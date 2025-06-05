# DeskAlerts API Java Example

This project provides a Java implementation for interacting with the DeskAlerts REST API to send alerts to specific users.

## Requirements

- **Java 11 or higher**: Required for using the `java.net.http` package.
- **Maven**: For building the project and managing dependencies.

## Setup

1. **Clone the Repository**: Clone this repository to your local machine or download the source code.
2. **Install Dependencies**: Open a terminal in the project directory (`c:\Git\api-examples\java`) and run:
   ```
   mvn install
   ```
   This will download the required dependencies specified in `pom.xml`, including Jackson for JSON processing and `dotenv-java` for loading environment variables.

## Enable API key auth in DeskAlerts

In order to use API key auth on your DeskAlerts server, please go to Settings > API > Enable API Secret key > Click Save and Copy the API key
You will use this key in the next steps.

## Environment Variables

The application uses environment variables for configuration to keep sensitive information like API keys secure. **Note**: Java does not automatically load `.env` files into environment variables like some other languages (e.g., Node.js). This project uses the `dotenv-java` library to load variables from a `.env` file if present.

1. **Create a `.env` file** (optional): Create a `.env` file in the project root with the following content:
   ```
   API_ENDPOINT=https://yourdeskalertsserver.com/DeskAlerts/api/
   API_KEY=your-api-key-here
   ```
   The `dotenv-java` library will load these values at runtime if the file exists.

2. **Set Environment Variables Manually** (if not using `.env` file):
   - On Windows, in a command prompt or PowerShell, set the variables before running the program:
     ```
     set API_ENDPOINT=https://yourdeskalertsserver.com/DeskAlerts/api/
     set API_KEY=your-api-key-here
     ```
   - On Unix-like systems (Linux/Mac), use:
     ```
     export API_ENDPOINT=https://yourdeskalertsserver.com/DeskAlerts/api/
     export API_KEY=your-api-key-here
     ```

## Compilation

To compile the project, run the following Maven command in the project directory:
```
mvn clean package
```
This will create an executable JAR file with dependencies included in the `target` folder.

## Sending an Alert

To send an alert, run the compiled JAR file or use Maven to execute the program directly:

1. **Using the JAR file**:
   ```
   java -jar target/original-send-alert-1.0.0.jar
   ```
   Ensure environment variables are set before running this command if you are using custom values.

2. **Using Maven**:
   ```
   mvn exec:java
   ```
   This command compiles and runs the program in one step.

The program will:
- Log in to the DeskAlerts API using the provided API key.
- Search for a user named "John Doe" to retrieve their user ID.
- Send an alert with a predefined message and title to the specified user.

## Debugging

If you encounter issues (e.g., HTTP 500 errors), the program prints the JSON request payload for the alert creation. Check the console output for debugging information.

## Customization

To change the target user, message, or title of the alert, modify the `main` method in `SendAlert.java`:
- Update the user name in the `getUser("John Doe")` call.
- Update the message and title in the `sendAlert(userId, message, title)` call.
