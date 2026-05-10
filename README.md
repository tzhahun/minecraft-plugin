# Minecraft Plugin

A Minecraft development project.

## Getting Started

This is a Maven-based Minecraft plugin project.

### Prerequisites

- Java 8 or higher
- Maven 3.6+
- Minecraft Server (for testing)

### Building

To build the project, run:

```bash
mvn clean package
```

The compiled plugin JAR will be located in the `target/` directory.

### Installation

1. Copy the generated JAR file from the `target/` directory
2. Paste it into your Minecraft server's `plugins/` folder
3. Restart the server

## Project Structure

```
minecraft-plugin/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── [your plugin code]
│   └── test/
│       └── java/
│           └── [your tests]
├── pom.xml
└── README.md
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.
