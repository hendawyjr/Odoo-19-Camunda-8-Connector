package io.camunda.connector.odoo.inbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

/**
 * Lightweight HTTP client for Odoo polling.
 * Uses the JSON-2 API to query records.
 * 
 * Uses Java's built-in HttpClient (java.net.http) to avoid classloader
 * conflicts
 * with the Camunda 8 Connector Runtime.
 */
public class OdooPollingClient implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(OdooPollingClient.class);
    private static final int CONNECT_TIMEOUT_SECONDS = 15;
    private static final int REQUEST_TIMEOUT_SECONDS = 30;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String database;
    private final String apiKey;

    public OdooPollingClient(String url, String database, String apiKey) {
        this.baseUrl = normalizeUrl(url) + "/json/2";
        this.database = database;
        this.apiKey = apiKey;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT_SECONDS))
                .build();
    }

    private String normalizeUrl(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    /**
     * Execute search_read to get records matching domain.
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> searchRead(String model, List<Object> domain,
            List<String> fields, int limit) throws OdooPollingException {
        String url = String.format("%s/%s/search_read", baseUrl, model);

        try {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("domain", domain != null ? domain : List.of());
            if (fields != null && !fields.isEmpty()) {
                body.put("fields", fields);
            }
            body.put("limit", limit);
            body.put("context", Map.of("lang", "en_US"));

            String jsonPayload = objectMapper.writeValueAsString(body);
            LOG.debug("Polling {} with domain: {}", model, domain);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("Authorization", "bearer " + apiKey)
                    .header("X-Odoo-Database", database)
                    .header("User-Agent", "Camunda-Odoo-Polling/1.0")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body() != null ? response.body() : "";

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                LOG.error("Odoo API error {}: {}", response.statusCode(), responseBody);
                throw new OdooPollingException("HTTP " + response.statusCode() + ": " + responseBody);
            }

            Object result = objectMapper.readValue(responseBody, Object.class);
            if (result instanceof List) {
                return (List<Map<String, Object>>) result;
            }
            return List.of();

        } catch (JsonProcessingException e) {
            throw new OdooPollingException("JSON error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new OdooPollingException("Network error: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OdooPollingException("Request interrupted", e);
        }
    }

    /**
     * Test the connection to Odoo.
     */
    public boolean testConnection() {
        try {
            searchRead("res.users", List.of(List.of("id", "=", 1)), List.of("name"), 1);
            return true;
        } catch (Exception e) {
            LOG.error("Connection test failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void close() {
        // HttpClient doesn't require explicit cleanup like OkHttp
        // The underlying resources are managed by the JVM
        LOG.debug("OdooPollingClient closed");
    }

    /**
     * Parse domain from JSON string.
     */
    public List<Object> parseDomain(String domainJson) {
        if (domainJson == null || domainJson.isBlank()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(domainJson, new TypeReference<List<Object>>() {
            });
        } catch (Exception e) {
            LOG.warn("Failed to parse domain JSON, using empty domain: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * Parse fields from JSON string.
     */
    public List<String> parseFields(String fieldsJson) {
        if (fieldsJson == null || fieldsJson.isBlank()) {
            return null; // null means all fields
        }
        try {
            return objectMapper.readValue(fieldsJson, new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            LOG.warn("Failed to parse fields JSON: {}", e.getMessage());
            return null;
        }
    }

    public static class OdooPollingException extends Exception {
        public OdooPollingException(String message) {
            super(message);
        }

        public OdooPollingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
