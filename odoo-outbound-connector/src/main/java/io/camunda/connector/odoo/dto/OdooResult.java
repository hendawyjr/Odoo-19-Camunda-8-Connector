package io.camunda.connector.odoo.dto;

import java.util.List;
import java.util.Map;

/**
 * Response DTO for Odoo outbound connector operations.
 * Provides structured output for each operation type.
 */
public record OdooResult(
        /** Operation that was performed */
        String operation,

        /** Model that was operated on */
        String model,

        /** Whether the operation was successful */
        boolean success,

        /** For CREATE: the ID of the created record */
        Integer createdId,

        /** For UPDATE/DELETE: list of affected record IDs */
        List<Integer> affectedIds,

        /** For READ/SEARCH_READ: the record data */
        List<Map<String, Object>> records,

        /** For SEARCH: list of matching record IDs */
        List<Integer> searchIds,

        /** For SEARCH_COUNT: number of matching records */
        Integer count,

        /** For CALL_METHOD: raw method result */
        Object methodResult,

        /** Error message if operation failed */
        String error,

        /** Odoo error name/type for debugging */
        String errorName,

        /** HTTP status code from Odoo */
        Integer statusCode) {
    // ==================== Factory Methods ====================

    public static OdooResult created(String model, Integer id) {
        return new OdooResult("CREATE", model, true, id, null, null, null, null, null, null, null, null);
    }

    public static OdooResult read(String model, List<Map<String, Object>> records) {
        return new OdooResult("READ", model, true, null, null, records, null, null, null, null, null, null);
    }

    public static OdooResult updated(String model, List<Integer> ids) {
        return new OdooResult("UPDATE", model, true, null, ids, null, null, null, null, null, null, null);
    }

    public static OdooResult deleted(String model, List<Integer> ids) {
        return new OdooResult("DELETE", model, true, null, ids, null, null, null, null, null, null, null);
    }

    public static OdooResult searched(String model, List<Integer> ids) {
        return new OdooResult("SEARCH", model, true, null, null, null, ids, null, null, null, null, null);
    }

    public static OdooResult searchedAndRead(String model, List<Map<String, Object>> records) {
        return new OdooResult("SEARCH_READ", model, true, null, null, records, null, null, null, null, null, null);
    }

    public static OdooResult counted(String model, Integer count) {
        return new OdooResult("SEARCH_COUNT", model, true, null, null, null, null, count, null, null, null, null);
    }

    public static OdooResult methodCalled(String model, String methodName, Object result) {
        return new OdooResult("CALL_METHOD:" + methodName, model, true, null, null, null, null, null, result, null,
                null, null);
    }

    public static OdooResult error(String operation, String model, String message) {
        return new OdooResult(operation, model, false, null, null, null, null, null, null, message, null, null);
    }

    public static OdooResult error(String operation, String model, String message, String errorName,
            Integer statusCode) {
        return new OdooResult(operation, model, false, null, null, null, null, null, null, message, errorName,
                statusCode);
    }

    // ==================== Utility Methods ====================

    /**
     * Get the primary result value depending on operation type.
     */
    public Object getPrimaryResult() {
        if (!success)
            return null;

        return switch (operation) {
            case "CREATE" -> createdId;
            case "READ", "SEARCH_READ" -> records;
            case "UPDATE", "DELETE" -> affectedIds;
            case "SEARCH" -> searchIds;
            case "SEARCH_COUNT" -> count;
            default -> {
                if (operation.startsWith("CALL_METHOD")) {
                    yield methodResult;
                }
                yield null;
            }
        };
    }

    /**
     * Check if the result contains any records.
     */
    public boolean hasRecords() {
        return records != null && !records.isEmpty();
    }

    /**
     * Get the first record (useful for single-record reads).
     */
    public Map<String, Object> getFirstRecord() {
        if (hasRecords()) {
            return records.get(0);
        }
        return null;
    }
}
