# Getting Started

This guide will help you install and configure the Camunda Odoo Connector.

## Prerequisites

1. **Camunda 8.9.0+** running (Self-Managed or SaaS)
2. **Odoo 19** instance with External API enabled
3. **Odoo API Key** for authentication

## Installation

### Option 1: Download from Releases

1. Go to the [Releases](../../releases) page
2. Download the latest JAR files:
   - `odoo-outbound-connector-X.X.X.jar`
   - `odoo-inbound-connector-X.X.X.jar`

### Option 2: Build from Source

```bash
# Clone the repository
git clone https://github.com/YOUR_ORG/Odoo-19-Camunda-8-Connector.git
cd Odoo-19-Camunda-8-Connector

# Build outbound connector
cd odoo-outbound-connector
mvn clean package -DskipTests

# Build inbound connector
cd ../odoo-inbound-connector
mvn clean package -DskipTests
```

## Deployment

### Self-Managed (Helm)

Add to your Helm values:

```yaml
connectors:
  extraEnv:
    - name: LOADER_PATH
      value: "/opt/custom-connectors/"
  extraVolumeMounts:
    - name: custom-connectors
      mountPath: /opt/custom-connectors
  extraVolumes:
    - name: custom-connectors
      hostPath:
        path: /path/to/your/connector-jars
```

### Docker

```bash
docker run -d \
  -v /path/to/jars:/opt/custom-connectors \
  -e LOADER_PATH=/opt/custom-connectors \
  camunda/connectors-bundle:8.9.0
```

## Install Element Templates

Copy element templates to Camunda Modeler:

| OS | Path |
|----|------|
| Windows | `%APPDATA%\camunda-modeler\resources\element-templates\` |
| macOS | `~/Library/Application Support/camunda-modeler/resources/element-templates/` |
| Linux | `~/.config/camunda-modeler/resources/element-templates/` |

Templates to install:
- `odoo-outbound-connector.json`
- `odoo-inbound-start-event.json`
- `odoo-inbound-intermediate.json`
- `odoo-inbound-connector-start-event.json` (webhook)
- `odoo-inbound-connector-intermediate.json` (webhook)
- `odoo-inbound-connector-boundary.json` (webhook)

## Odoo API Key Setup

1. Log in to Odoo as administrator
2. Go to **Settings → General Settings → Integrations**
3. Enable **External API**
4. Navigate to **Settings → Users → [Your User] → API Keys**
5. Click **New API Key**
6. Copy the generated key

## Next Steps

- [Outbound Connector](Outbound-Connector) - Learn how to send data to Odoo
- [Inbound Connector](Inbound-Connector) - Learn how to receive events from Odoo
- [Configuration](Configuration) - Detailed configuration options
