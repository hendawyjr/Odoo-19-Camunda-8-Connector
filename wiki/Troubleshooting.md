# Troubleshooting

Common issues and solutions for the Odoo Connector.

## Authentication Errors

### 401 Unauthorized

**Error:**
```json
{"error": "[401] Unauthorized", "statusCode": 401}
```

**Causes:**
- Invalid API key
- API key expired or revoked
- External API not enabled in Odoo

**Solutions:**
1. Verify API key is correct
2. Generate a new API key in Odoo
3. Enable External API: Settings → General Settings → Integrations

### 403 Forbidden

**Error:**
```json
{"error": "[403] Access Denied", "statusCode": 403}
```

**Causes:**
- User lacks permissions for the model
- Record-level access rules blocking

**Solutions:**
1. Check user's access rights in Odoo
2. Verify model permissions (Settings → Users → Access Rights)
3. Check record rules for the model

---

## Request Errors

### 422 Missing Required Argument

**Error:**
```json
{"error": "[422] missing a required argument: 'vals_list'", "statusCode": 422}
```

**Causes:**
- Missing required fields for CREATE
- Empty values object

**Solutions:**
1. Provide all required fields for the model
2. For `res.partner`, `name` is required:
```json
{"values": {"name": "Required Name"}}
```

### 404 Not Found

**Error:**
```json
{"error": "[404] Record not found", "statusCode": 404}
```

**Causes:**
- Record ID doesn't exist
- Record was deleted
- Wrong model name

**Solutions:**
1. Verify record exists in Odoo
2. Check model name spelling (e.g., `res.partner` not `res_partner`)

---

## Inbound Connector Issues

### Connector Not Discovered

**Symptoms:**
- Connector not available in Modeler
- "Unknown connector type" error

**Solutions:**
1. Verify JAR is in `LOADER_PATH` directory
2. Check connector runtime logs for errors
3. Restart connector runtime after adding JAR

### Polling Not Triggering

**Symptoms:**
- No events received
- Polling seems inactive

**Solutions:**
1. Verify Odoo credentials are correct
2. Check `trackingField` exists on model
3. Ensure records have been modified after process started
4. Check runtime logs for polling errors

### Webhook Not Receiving Events

**Solutions:**
1. Verify webhook URL is accessible from Odoo
2. Check firewall/network allows inbound connections
3. Verify Odoo is sending to correct URL
4. Check connector runtime logs

---

## Build Errors

### Java Version Error

**Error:**
```
error: release version 21 not supported
```

**Solution:**
```bash
# Set JAVA_HOME to Java 21
export JAVA_HOME=/path/to/java21
mvn clean package
```

### Dependency Resolution Error

**Solution:**
```bash
mvn clean install -U
```

---

## Deployment Errors

### ClassNotFoundException

**Error:**
```
java.lang.ClassNotFoundException: io.camunda.connector.odoo...
```

**Solutions:**
1. Verify JAR file is complete (not corrupted)
2. Check JAR is in correct `LOADER_PATH`
3. Rebuild connector:
```bash
mvn clean package -DskipTests
```

### Filename Too Long (Windows)

**Error:**
```
error: lstat: Filename too long
```

**Solution:**
```bash
git config --global core.longpaths true
```

---

## Common Odoo Models

| Model | Purpose | Required Fields |
|-------|---------|-----------------|
| `res.partner` | Contacts | `name` |
| `sale.order` | Sales Orders | `partner_id` |
| `purchase.order` | Purchase Orders | `partner_id` |
| `product.product` | Products | `name` |
| `account.move` | Invoices | `partner_id`, `move_type` |
| `stock.picking` | Inventory | `picking_type_id` |

---

## Getting Help

1. **Check Logs**: Enable DEBUG logging for detailed information
2. **GitHub Issues**: [Report bugs](../../issues)
3. **Camunda Forum**: [Community support](https://forum.camunda.io/)
4. **Odoo Forums**: [Odoo community](https://www.odoo.com/forum)
