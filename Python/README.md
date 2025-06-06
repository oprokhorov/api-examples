# DeskAlerts API Python Example

This repository contains a Python script demonstrating how to interact with the DeskAlerts API to send alerts to specific users.

## Overview

The `send_alert.py` script logs into the DeskAlerts system using an API key, searches for a user named "John Doe", retrieves their user ID, and sends a test alert to that user.

## Prerequisites

- Python 3.6 or higher
- `requests` library (install via `pip install requests`)
- DeskAlerts API endpoint URL and API key

## Setup

1. **Environment Variables**: Set the following environment variables in your system or in your terminal session before running the script:
   - `DESKALERTS_API_ENDPOINT`: Your DeskAlerts API endpoint URL (e.g., `https://yourdeskalertsserver.com/DeskAlerts/api/`).
   - `DESKALERTS_API_KEY`: Your DeskAlerts API secret key.

   You can set these temporarily in your terminal session with:
   ```bash
   export DESKALERTS_API_ENDPOINT="https://yourdeskalertsserver.com/DeskAlerts/api/"
   export DESKALERTS_API_KEY="your-api-key"
   ```

2. **Install Dependencies**: Ensure the `requests` library is installed:
   ```bash
   pip install requests
   ```

## Sending an Alert

1. Open a terminal.
2. Navigate to the directory containing the script:
   ```bash
   cd path/to/api-examples/python
   ```
3. Run the script:
   ```bash
   python send_alert.py
   ```
4. Check the output for the response from the alert creation API call to confirm the alert was sent successfully.
