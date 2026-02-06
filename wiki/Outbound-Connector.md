# Outbound Connector

The Outbound Connector allows you to perform operations on Odoo from your Camunda workflows.

## Authentication

| Property | Description | Example |
|----------|-------------|---------|
| `url` | Odoo instance URL | `https://mycompany.odoo.com` |
| `database` | Database name | `mycompany` |
| `apiKey` | API key | `{{secrets.ODOO_API_KEY}}` |

## Operations

### CREATE

Create a new record in Odoo.

**Input:**
```json
{
  "operation": "CREATE",
  "model": "res.partner",
  "values": {
    "name": "Acme Corporation",
    "email": "info@acme.com",
    "phone": "+1-555-0123",
    "is_company": true
  }
}
```

**Output:**
```json
{
  "success": true,
  "createdId": 42,
  "primaryResult": 42
}
```

---

### READ

Read records by IDs.

**Input:**
```json
{
  "operation": "READ",
  "model": "res.partner",
  "recordIds": [1, 2, 3],
  "fields": ["name", "email", "phone"]
}
```

**Output:**
```json
{
  "success": true,
  "records": [
    {"id": 1, "name": "Partner 1", "email": "p1@example.com"},
    {"id": 2, "name": "Partner 2", "email": "p2@example.com"}
  ]
}
```

---

### SEARCH

Find record IDs matching criteria.

**Input:**
```json
{
  "operation": "SEARCH",
  "model": "res.partner",
  "domain": [["is_company", "=", true]],
  "limit": 10,
  "offset": 0,
  "order": "name ASC"
}
```

**Output:**
```json
{
  "success": true,
  "searchIds": [1, 5, 12, 23],
  "count": 4
}
```

---

### SEARCH_READ

Search and read in one operation.

**Input:**
```json
{
  "operation": "SEARCH_READ",
  "model": "sale.order",
  "domain": [["state", "=", "sale"]],
  "fields": ["name", "partner_id", "amount_total"],
  "limit": 50
}
```

---

### UPDATE

Update existing records.

**Input:**
```json
{
  "operation": "UPDATE",
  "model": "res.partner",
  "recordIds": [42],
  "values": {
    "phone": "+1-555-9999",
    "comment": "Updated via Camunda"
  }
}
```

---

### DELETE

Delete records.

**Input:**
```json
{
  "operation": "DELETE",
  "model": "res.partner",
  "recordIds": [42, 43]
}
```

---

### EXECUTE

Call custom Odoo methods.

**Input:**
```json
{
  "operation": "EXECUTE",
  "model": "sale.order",
  "methodName": "action_confirm",
  "recordId": 100
}
```

## Domain Filter Syntax

Odoo uses Polish notation for domain filters:

```json
// Simple: field = value
[["state", "=", "draft"]]

// AND (implicit)
[["state", "=", "draft"], ["amount", ">", 1000]]

// OR
["|", ["state", "=", "draft"], ["state", "=", "sent"]]

// NOT
["!", ["active", "=", false]]
```

### Operators

| Operator | Description |
|----------|-------------|
| `=`, `!=` | Equals / Not equals |
| `>`, `<`, `>=`, `<=` | Comparisons |
| `like` | Pattern match (% wildcards) |
| `ilike` | Case-insensitive pattern |
| `in`, `not in` | Value in list |
| `child_of` | Hierarchical child |

## Common Models

| Model | Description |
|-------|-------------|
| `res.partner` | Contacts & Companies |
| `sale.order` | Sales Orders |
| `purchase.order` | Purchase Orders |
| `account.move` | Invoices & Bills |
| `product.product` | Products |
| `stock.picking` | Inventory Transfers |
| `hr.employee` | Employees |
| `project.project` | Projects |
| `project.task` | Tasks |
