import os
import requests
import json

# Get environment variables
api_endpoint = os.getenv("DESKALERTS_API_ENDPOINT")
api_secret_key = os.getenv("DESKALERTS_API_KEY")

# Check if environment variables are set
if not api_endpoint or not api_secret_key:
    print("Error: DESKALERTS_API_ENDPOINT and DESKALERTS_API_KEY environment variables must be set.")
    exit(1)

# Create a session to persist cookies across requests
session = requests.Session()

# Login request data
login_data = {
    "key": api_secret_key
}

# Log in
print("Logging in...")
login_url = f"{api_endpoint}account/apikey"
login_response = session.post(login_url, json=login_data)
login_response.raise_for_status()
print("Login Response Status:", login_response.status_code)

# "John Doe" user search data
user_search_data = {
    "UserName": "John Doe"
}

# Search for user "John Doe"
print("Searching for user 'John Doe'...")
user_url = f"{api_endpoint}organization/user"
user_response = session.post(user_url, json=user_search_data)
user_response.raise_for_status()
user_data = user_response.json()
print("User Search Response:", json.dumps(user_data, indent=2))

# Get user ID
user_id = user_data.get("data", {}).get("userId")
if not user_id:
    print("Failed to retrieve user ID for 'John Doe'")
    exit(1)
print("Found user ID:", user_id)

# Compose the alert data for "John Doe"
alert_data = {
    "body": "This alert was sent through DeskAlerts 11 REST API to a user John Doe",
    "title": "(Python) DeskAlerts 11 REST API test",
    "recipients": [
        {
            "id": user_id,
            "type": "User"
        }
    ],
    "extraRecipients": []
}

# Send the alert
print("Sending alert...")
alert_url = f"{api_endpoint}publisher/alert/create"
alert_response = session.post(alert_url, json=alert_data)
alert_response.raise_for_status()
alert_result = alert_response.json()
print("Alert Creation Response:", json.dumps(alert_result, indent=2))
