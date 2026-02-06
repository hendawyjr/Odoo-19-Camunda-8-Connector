# Odoo 19 Inbound Connector

Production-grade Camunda 8 Inbound Connector that polls Odoo 19 for record changes.

## How It Works

Unlike webhooks (which Odoo doesn't natively support), this connector **polls** Odoo at regular intervals:

```
[Odoo Database] <--poll every N seconds-- [Camunda Connector]
                  (new/modified records)        |
                                                v
                                    [Process Instance Correlation]
```

## Features

- **Polling-based** - No webhook setup required in Odoo
- **Configurable interval** - 10 seconds minimum
- **Trigger conditions** - New records only, modified only, or both
- **Custom filters** - Odoo domain filter support
- **Duplicate detection** - Won't process same record twice
- **Graceful shutdown** - Proper cleanup on deactivation

## Build

```bash
mvn clean package -DskipTests
```

## Configuration

### Authentication
| Parameter | Description |
|-----------|-------------|
| **URL** | Odoo instance URL |
| **Database** | Database name |
| **API Key** | API key from Odoo user profile |

### Polling
| Parameter | Description | Default |
|-----------|-------------|---------|
| **Model** | Odoo model to monitor (e.g., `sale.order`) | Required |
| **Polling Interval** | Seconds between polls | 30 |
| **Trigger Condition** | NEW, MODIFIED, or BOTH | BOTH |
| **Trigger Field** | Field to track changes | write_date |
| **Filter Domain** | Additional domain filter | None |
| **Fields** | Fields to include in event | All |
| **Batch Size** | Max records per poll | 50 |

## Event Variables

On correlation, these variables are available:
- `odooModel` - Model name (e.g., "sale.order")
- `odooRecordId` - Record ID
- `odooEventType` - "create" or "write"
- `odooRecord` - Full record data
- `odooTimestamp` - When record was modified
- `odooFields` - List of fields included

## Usage Examples

### Start Process on New Sale Order
```
Model: sale.order
Trigger: NEW
Filter: [["state", "=", "sale"]]
Fields: ["name", "partner_id", "amount_total"]
```

### Wait for Partner Update
```
Model: res.partner
Trigger: MODIFIED
Filter: [["id", "=", ${partnerId}]]
Correlation Key: =odooRecordId
```

## Element Templates

Three templates for different BPMN elements:
- `odoo-inbound-start-event.json` - Message Start Event
- `odoo-inbound-intermediate.json` - Intermediate Catch Event

## Deployment

1. Copy JAR to Connectors runtime
2. Upload element templates to Modeler
3. Configure Odoo credentials

## Notes

- **Polling Interval**: Don't set too low (< 10s) to avoid overloading Odoo
- **Batch Size**: Keep reasonable (50) for performance
- **Write Date**: Always uses UTC for timestamp comparison
