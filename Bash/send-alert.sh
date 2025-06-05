#!/bin/bash
# requires jq for JSON parsing

API_ENDPOINT=${DESKALERTS_API_ENDPOINT}
API_SECRET_KEY=${DESKALERTS_API_KEY}
COOKIE_FILE="cookie.txt"

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    echo "Error: 'jq' is not installed. It is required for JSON parsing. Please install jq (e.g., 'sudo apt install jq' on Debian-based systems)."
    exit 1
fi

# Check if environment variables are set
if [ -z "$API_ENDPOINT" ] || [ -z "$API_SECRET_KEY" ]; then
    echo "Error: DESKALERTS_API_ENDPOINT and DESKALERTS_API_KEY environment variables must be set."
    exit 1
fi

# Login request data
LOGIN_BODY=$(cat <<EOF
{
    "key": "$API_SECRET_KEY"
}
EOF
)

# Log in
echo "Logging in..."
LOGIN_RESPONSE=$(curl \
    --silent \
    --cookie-jar $COOKIE_FILE \
    --location "${API_ENDPOINT}account/apikey" \
    --header 'Content-Type: application/json' \
    --request "POST" \
    --data "$LOGIN_BODY"
)
echo "Login Response:"
echo "$LOGIN_RESPONSE" | jq .

# "John Doe" user search data
USER_SEARCH_BODY=$(cat <<EOF
{
    "UserName": "John Doe"
}
EOF
)

# Search for user "John Doe"
echo "Searching for user 'John Doe'..."
USER_RESPONSE=$(curl \
    --silent \
    --cookie $COOKIE_FILE \
    --location "${API_ENDPOINT}organization/user" \
    --header 'Content-Type: application/json' \
    --request "POST" \
    --data "$USER_SEARCH_BODY"
)
echo "User Search Response:"
echo "$USER_RESPONSE" | jq .

# Extract userid from response (requires jq for JSON parsing)
USER_ID=$(echo "$USER_RESPONSE" | jq -r '.data.userId')

if [ -z "$USER_ID" ] || [ "$USER_ID" == "null" ]; then
    echo "Failed to retrieve user ID for 'John Doe': $USER_RESPONSE"
    exit 1
fi
echo "Found user ID: $USER_ID"

# Compose the alert data for "John Doe"
ALERT_BODY=$(cat <<EOF
{
    "body": "This alert was sent through DeskAlerts 11 REST API to a user John Doe",
    "title": "(Bash) DeskAlerts 11 REST API test",
    "extraRecipients": [],
    "recipients": [
        {
            "id": $USER_ID,
            "type": "User"
        }
    ]
}
EOF
)

# Send the alert
echo "Sending alert..."
ALERT_RESPONSE=$(curl \
    --silent \
    --cookie $COOKIE_FILE \
    --location "${API_ENDPOINT}publisher/alert/create" \
    --header 'Content-Type: application/json' \
    --request "POST" \
    --data "$ALERT_BODY"
)
echo "Alert Creation Response:"
echo "$ALERT_RESPONSE" | jq .

# Clean up cookie file
rm -f $COOKIE_FILE