# Welcome to the Camunda Odoo Connector Wiki

Welcome to the official documentation for the **Camunda Odoo Connector** - a production-ready integration between Camunda 8 and Odoo 19 ERP.

## Quick Links

- [Home](Home) - This page
- [Getting Started](Getting-Started) - Installation and setup
- [Outbound Connector](Outbound-Connector) - Send data to Odoo
- [Inbound Connector](Inbound-Connector) - Receive data from Odoo
- [Configuration](Configuration) - Detailed configuration options
- [Troubleshooting](Troubleshooting) - Common issues and solutions

## Features

### Outbound Connector (Camunda → Odoo)
| Operation | Description |
|-----------|-------------|
| CREATE | Create new records in any Odoo model |
| READ | Read records by IDs |
| SEARCH | Find records with domain filters |
| SEARCH_READ | Combined search and read |
| UPDATE | Update existing records |
| DELETE | Delete records |
| EXECUTE | Call custom Odoo methods |

### Inbound Connector (Odoo → Camunda)
| Mode | Description |
|------|-------------|
| Webhook | Real-time events via HTTP POST |
| Polling | Periodic polling for changes |

## Requirements

- **Camunda 8.9.0+** (Self-Managed or SaaS)
- **Odoo 19** with External API enabled
- **Java 21+** (for building from source)

## Support

- [GitHub Issues](../../issues) - Report bugs or request features
- [Camunda Forum](https://forum.camunda.io/) - Community support
- [Odoo Documentation](https://www.odoo.com/documentation/19.0/developer/reference/external_api.html)
