# Inbound Connector

The Inbound Connector allows Odoo to trigger Camunda workflows via webhooks or polling.

## Modes

| Mode | Description | Use Case |
|------|-------------|----------|
| **Webhook** | Real-time HTTP events | Instant notifications |
| **Polling** | Periodic checks | No Odoo modification needed |

---

## Webhook Mode

Receives HTTP POST events from Odoo in real-time.

### Configuration

| Property | Description | Example |
|----------|-------------|---------|
| `mode` | Connector mode | `WEBHOOK` |
| `context` | Webhook URL path | `odoo-events` |
| `expectedModels` | Models to accept | `sale.order,res.partner` |

### Webhook URL

Your webhook URL will be:
```
https://your-camunda-instance/inbound/{context}
```

### Odoo Webhook Setup

Configure Odoo to send webhooks (requires Odoo customization or automation rules):

```python
# Example: Odoo Automated Action
import requests
import json

webhook_url = "https://camunda.example.com/inbound/odoo-events"
payload = {
    "model": "sale.order",
    "operation": "create",
    "record_id": record.id,
    "timestamp": fields.Datetime.now().isoformat(),
    "data": {
        "name": record.name,
        "partner_id": record.partner_id.id,
        "amount_total": record.amount_total
    }
}

requests.post(webhook_url, json=payload, timeout=10)
```

### Payload Format

```json
{
  "model": "sale.order",
  "operation": "create",
  "record_id": 123,
  "timestamp": "2024-01-15T10:30:00Z",
  "data": {
    "name": "SO001",
    "partner_id": 42,
    "amount_total": 1500.00
  }
}
```

### Event Types

| Event Type | BPMN Element |
|------------|--------------|
| Start Event | Start process when webhook received |
| Intermediate Catch | Wait mid-process for webhook |
| Boundary Event | Catch webhook on activity boundary |

---

## Polling Mode

Periodically polls Odoo for new or modified records.

### Configuration

| Property | Description | Example |
|----------|-------------|---------|
| `mode` | Connector mode | `POLLING` |
| `url` | Odoo URL | `https://mycompany.odoo.com` |
| `database` | Database name | `mycompany` |
| `apiKey` | API key | `{{secrets.ODOO_API_KEY}}` |
| `model` | Model to poll | `sale.order` |
| `pollingInterval` | Interval (seconds) | `60` |
| `domain` | Filter domain | `[["state", "=", "sale"]]` |
| `trackingField` | Change detection field | `write_date` |

### How Polling Works

1. Connector polls Odoo at configured interval
2. Queries records where `trackingField > lastPollTime`
3. For each new/modified record, triggers correlation
4. Stores last poll time to avoid duplicates

### Event Types

| Event Type | BPMN Element |
|------------|--------------|
| Start Event | Start process for each polled record |
| Intermediate Catch | Wait mid-process for polled event |

---

## Correlation

### Message Correlation

Use correlation keys to route events to specific process instances:

| Property | Description |
|----------|-------------|
| `correlationKey` | FEEL expression for correlation |
| `messageIdExpression` | Unique message ID (de-duplication) |
| `resultVariable` | Variable to store event data |
| `resultExpression` | FEEL expression for result mapping |

### Example Correlation

```
Correlation Key Expression: = orderId
Message ID Expression: = model + "_" + record_id + "_" + timestamp
Result Expression: = { orderId: record_id, orderData: data }
```

---

## Output Variables

Both modes provide these variables:

| Variable | Description |
|----------|-------------|
| `model` | Odoo model name |
| `operation` | Event type (create/update/delete) |
| `recordId` | Record ID in Odoo |
| `timestamp` | Event timestamp |
| `data` | Record data (if included) |
