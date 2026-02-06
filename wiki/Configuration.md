# Configuration

Detailed configuration options for the Odoo Connector.

## Secrets Management

Use Camunda secrets for sensitive values like API keys.

### In BPMN
```
{{secrets.ODOO_API_KEY}}
```

### In Connector Runtime (Environment)
```yaml
env:
  - name: CAMUNDA_CONNECTORS_SECRETPROVIDER_ENVIRONMENT_PREFIX
    value: "CONNECTOR_"
  - name: CONNECTOR_ODOO_API_KEY
    value: "your-api-key-here"
```

### In Connector Runtime (HashiCorp Vault)
```yaml
env:
  - name: CAMUNDA_CONNECTORS_SECRETPROVIDER_VAULT_ENABLED
    value: "true"
  - name: VAULT_ADDR
    value: "https://vault.example.com"
  - name: VAULT_TOKEN
    value: "your-vault-token"
```

---

## Outbound Connector Properties

### Authentication

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `authentication.url` | String | ✅ | Odoo instance URL |
| `authentication.database` | String | ✅ | Database name |
| `authentication.apiKey` | String | ✅ | API key |

### Operation Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `operation` | Enum | ✅ | CREATE, READ, SEARCH, SEARCH_READ, UPDATE, DELETE, EXECUTE |
| `model` | String | ✅ | Odoo model name (e.g., `res.partner`) |
| `values` | Object | For CREATE/UPDATE | Field values |
| `recordId` | Integer | For single record | Single record ID |
| `recordIds` | Array | For multiple records | List of record IDs |
| `fields` | Array | For READ operations | Fields to return |
| `domain` | Array | For SEARCH | Filter domain |
| `limit` | Integer | Optional | Max records (default: 100) |
| `offset` | Integer | Optional | Skip records (default: 0) |
| `order` | String | Optional | Sort order (e.g., `name ASC`) |
| `methodName` | String | For EXECUTE | Custom method name |
| `methodArgs` | Object | For EXECUTE | Method arguments |
| `context` | Object | Optional | Odoo context |

---

## Inbound Connector Properties

### Polling Mode

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `inbound.mode` | Enum | ✅ | `POLLING` |
| `authentication.url` | String | ✅ | Odoo URL |
| `authentication.database` | String | ✅ | Database |
| `authentication.apiKey` | String | ✅ | API key |
| `polling.model` | String | ✅ | Model to poll |
| `polling.interval` | Integer | ✅ | Seconds between polls |
| `polling.domain` | Array | Optional | Filter domain |
| `polling.trackingField` | String | Optional | Field for change detection |
| `polling.fields` | Array | Optional | Fields to include |

### Webhook Mode

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `inbound.mode` | Enum | ✅ | `WEBHOOK` |
| `webhook.context` | String | ✅ | URL path context |
| `webhook.expectedModels` | String | Optional | Allowed models (comma-separated) |
| `webhook.secretKey` | String | Optional | Signature verification key |

### Correlation Properties

| Property | Type | Description |
|----------|------|-------------|
| `correlationKeyExpression` | FEEL | Expression for correlation key |
| `messageIdExpression` | FEEL | Unique message ID |
| `resultVariable` | String | Variable name for result |
| `resultExpression` | FEEL | Transform result data |

---

## Response Properties

### Outbound Response

| Property | Type | Description |
|----------|------|-------------|
| `success` | Boolean | Operation success |
| `operation` | String | Operation performed |
| `model` | String | Odoo model |
| `createdId` | Integer | Created record ID |
| `records` | Array | Read records |
| `searchIds` | Array | Found record IDs |
| `count` | Integer | Result count |
| `affectedIds` | Array | Updated/deleted IDs |
| `methodResult` | Object | Custom method result |
| `error` | String | Error message |
| `errorName` | String | Error type |
| `statusCode` | Integer | HTTP status code |
| `primaryResult` | Object | Main result value |
| `firstRecord` | Object | First record |

---

## Runtime Configuration

### Timeouts

The connector uses these default timeouts:

| Setting | Default | Description |
|---------|---------|-------------|
| Connect timeout | 30s | Connection establishment |
| Request timeout | 60s | Request completion |
| Max retries | 3 | Automatic retries |
| Retry delay | 1s | Delay between retries |

### Logging

Enable debug logging:

```yaml
logging:
  level:
    io.camunda.connector.odoo: DEBUG
```
