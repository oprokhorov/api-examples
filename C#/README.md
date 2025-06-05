# DeskAlerts API C# Example

This repository contains a C# application demonstrating how to interact with the DeskAlerts API to send alerts to specific users.

## Overview

The `SendAlert` application logs into the DeskAlerts system using an API key, searches for a user named "John Doe", retrieves their user ID, and sends a test alert to that user.

## Prerequisites

- .NET SDK 9.0 or higher
- DeskAlerts API endpoint URL and API key

## Enable API key auth in DeskAlerts

In order to use API key auth on your DeskAlerts server, please go to Settings > API > Enable API Secret key > Click Save and Copy the API key
You will use this key in the next steps.

## Setup

1. **Environment Variables**: Set the following environment variables in your system or in your development environment before running the application:
   - `DESKALERTS_API_ENDPOINT`: Your DeskAlerts API endpoint URL (e.g., `https://yourdeskalertsserver.com/DeskAlerts/api/`).
   - `DESKALERTS_API_KEY`: Your DeskAlerts API secret key.

   You can set these temporarily in a command prompt or PowerShell session with:
   ```powershell
   set DESKALERTS_API_ENDPOINT=https://yourdeskalertsserver.com/DeskAlerts/api/
   set DESKALERTS_API_KEY=your-api-key-here
   ```

2. **Clone the Repository**: If you haven't already, clone this repository to your local machine.

## Sending an Alert

1. Open a terminal or command prompt.

2. Run the application:
   ```powershell
   dotnet run .\SendAlert.csproj
   ```
3. Check the console output for the response from the alert creation API call to confirm the alert was sent successfully.

## License

This example is provided for educational purposes. Ensure you comply with DeskAlerts' terms of use and licensing.
