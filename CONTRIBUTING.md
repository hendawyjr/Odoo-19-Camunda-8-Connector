# Contributing to Camunda Odoo Connector

Thank you for your interest in contributing to the Camunda Odoo Connector! This document provides guidelines and instructions for contributing.

## Code of Conduct

By participating in this project, you agree to abide by the [Camunda Code of Conduct](https://camunda.com/code-of-conduct/).

## How to Contribute

### Reporting Issues

Before creating an issue, please:
1. Search existing issues to avoid duplicates
2. Use the issue templates when available
3. Include as much detail as possible:
   - Camunda version
   - Odoo version
   - Java version
   - Steps to reproduce
   - Expected vs actual behavior
   - Error messages and stack traces

### Suggesting Features

Feature requests are welcome! Please:
1. Describe the use case clearly
2. Explain why existing functionality doesn't meet your needs
3. If possible, suggest implementation approaches

### Pull Requests

1. **Fork the repository** and create your branch from `main`
2. **Follow coding standards**:
   - Use Java 21 features appropriately
   - Follow existing code style
   - Add JavaDoc for public APIs
   - Keep methods focused and small
3. **Write tests** for new functionality
4. **Update documentation** as needed
5. **Test locally** before submitting

#### PR Checklist
- [ ] Code compiles without errors
- [ ] All tests pass
- [ ] New code has appropriate test coverage
- [ ] Documentation is updated
- [ ] Commit messages are clear and descriptive
- [ ] Element templates are updated (if applicable)

## Development Setup

### Prerequisites
- Java 21+
- Maven 3.8+
- An Odoo 19 instance for testing

### Building

```bash
# Build outbound connector
cd odoo-outbound-connector
mvn clean package

# Build inbound connector
cd ../odoo-inbound-connector
mvn clean package
```

### Testing

```bash
# Run tests
mvn test

# Run with integration tests (requires Odoo)
mvn verify -Podoo-integration
```

### Code Style

We follow standard Java conventions with these additions:
- Use `var` for local variables when type is obvious
- Prefer records for DTOs
- Use pattern matching where applicable
- Keep lines under 120 characters

## Project Structure

```
camunda-odoo-connector/
├── odoo-outbound-connector/
│   ├── src/main/java/          # Java source code
│   ├── src/main/resources/     # Resources
│   ├── element-templates/      # Camunda Modeler templates
│   └── pom.xml
├── odoo-inbound-connector/
│   ├── src/main/java/
│   ├── src/main/resources/
│   ├── element-templates/
│   └── pom.xml
├── README.md
├── LICENSE
└── CONTRIBUTING.md
```

## Release Process

Releases are managed by maintainers. The process:
1. Update version in `pom.xml`
2. Update CHANGELOG.md
3. Create a release tag
4. GitHub Actions builds and publishes artifacts

## Questions?

- Open a [Discussion](https://github.com/your-org/camunda-odoo-connector/discussions)
- Join the [Camunda Forum](https://forum.camunda.io/)

Thank you for contributing! 🎉
