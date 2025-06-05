# Define your DeskAlerts API endpoint and credentials

$APIEndpoint = $Env:DESKALERTS_API_ENDPOINT
$APISecretKey = $Env:DESKALERTS_API_KEY

# Define a session variable to store cookies in
$session = New-Object Microsoft.PowerShell.Commands.WebRequestSession

# Login request data
$Body = @{
    key = "$APISecretKey"
} | ConvertTo-Json

# Log in
$URL = $APIEndpoint + "account/apikey"
$response = Invoke-RestMethod -Uri $URL -Method Post -Body $Body -WebSession $Session -ContentType "application/json"

# "John Doe" user search data
$Body = @{
    UserName     = "John Doe"
} | ConvertTo-Json

# Serch for user "John Doe"
$URL =  $APIEndpoint + "organization/user"
$response = Invoke-RestMethod -Uri $URL -Method Post -Body $Body -WebSession $Session -ContentType "application/json"

# Get it's userid
$UserId = $response.data.userid

# Compose the alert data for "John Doe"
$Body = @{
    body     = "This alert was sent through DeskAlerts 11 REST API to a user John Doe"
    title = "(PowerShell) DeskAlerts 11 REST API test"
    recipients = @(
        @{
            id   = $UserId
            type = "User"
        }
    )
    extraRecipients = @()
} | ConvertTo-Json -Depth 3

# Send the alert
$URL =  $APIEndpoint + "publisher/alert/create"
$response = Invoke-RestMethod -Uri $URL -Method Post -Body $Body -WebSession $Session -ContentType "application/json"
Write-Output "Alert Creation Response: $response"