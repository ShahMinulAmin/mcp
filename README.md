## Prerequisite
- JDK version 21
- Docker environment to run as a stand-alone server

## To run locally
- Build the project
  ```
  mvn clean package -DskipTests
  ```
- A jar file `mcp-0.0.1-SNAPSHOT.jar` will be generated in target directory
- Configure host application by adding java command with arguments
  ```
    "shah-mcp": {
    "command": "java",
    "args": [
      "-Dspring.profiles.active=stdio",
      "-jar",
      "..../spring-ai/mcp/target/mcp-0.0.1-SNAPSHOT.jar"
    ]
  }
  ```

## To run as a stand-alone server
- Docker compose command will build the docker image and start running as a stand-alone server
  ```
  docker-compose up
  ```

## Acknowledgement
- This project is inspired from [GitHub repo](https://github.com/spring-projects/spring-ai-examples/tree/main/model-context-protocol/)

## Resources
- [modelcontextprotocol](https://modelcontextprotocol.io/docs/learn/architecture)
- [json-rpc](https://docs.moralis.com/how-json-rpc-works)
