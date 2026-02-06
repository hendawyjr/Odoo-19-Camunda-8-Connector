package io.camunda.connector.odoo.inbound;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Spring Boot Auto-Configuration for Odoo Inbound Connectors.
 * IMPORTANT: Inbound connectors MUST be prototype scope for proper lifecycle
 * management.
 */
@Configuration
public class OdooInboundConnectorAutoConfiguration {

    @Bean
    @Scope("prototype")
    public OdooPollingExecutable odooPollingExecutable() {
        return new OdooPollingExecutable();
    }

    @Bean
    @Scope("prototype")
    public OdooInboundExecutable odooInboundWebhookExecutable() {
        return new OdooInboundExecutable();
    }
}
