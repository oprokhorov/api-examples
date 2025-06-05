# DeskAlerts API PowerShell Example

This repository contains a PowerShell script demonstrating how to interact with the DeskAlerts API to send alerts to specific users.

## Overview

The `Send-Alert.ps1` script logs into the DeskAlerts system using an API key, searches for a user named "John Doe", retrieves their user ID, and sends a test alert to that user.

## Prerequisites

- PowerShell 5.1 or higher
- DeskAlerts API endpoint URL and API key

## Enable API key auth in DeskAlerts

In order to use API key auth on your DeskAlerts server, please go to Settings > API > Enable API Secret key > Click Save and Copy the API key
You will use this key in the next steps.

## Setup

1. **Environment Variables**: Set the following environment variables in your system or in your PowerShell session before running the script:
   - `DESKALERTS_API_ENDPOINT`: Your DeskAlerts API endpoint URL (e.g., `https://your-deskalerts-domain/api/`).
   - `DESKALERTS_API_KEY`: Your DeskAlerts API secret key.

   You can set these temporarily in your PowerShell session with:
   ```powershell
   $Env:DESKALERTS_API_ENDPOINT = "https://your-deskalerts-domain/api/"
   $Env:DESKALERTS_API_KEY = "your-api-key"
   ```

## Sending an Alert

1. Open a PowerShell terminal.
2. Run the script:
   ```powershell
   .\Send-Alert.ps1
   ```
3. Check the output for the response from the alert creation API call to confirm the alert was sent successfully.

## Script Details

- **Authentication**: The script logs in using the provided API key.
- **User Search**: Searches for a user named "John Doe".
- **Alert Sending**: Sends a test alert titled "(PowerShell) DeskAlerts 11 REST API test" to the found user.

## License

This example is provided for educational purposes. Ensure you comply with DeskAlerts' terms of use and licensing.
