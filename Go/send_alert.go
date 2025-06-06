package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/http/cookiejar"
	"os"
)

// LoginRequest represents the structure for the login API request
type LoginRequest struct {
	Key string `json:"key"`
}

// UserSearchRequest represents the structure for the user search API request
type UserSearchRequest struct {
	UserName string `json:"UserName"`
}

// UserSearchResponse represents the structure for the user search API response
type UserSearchResponse struct {
	Data struct {
		UserID int `json:"userId"`
	} `json:"data"`
}

// AlertRequest represents the structure for the alert creation API request
type AlertRequest struct {
	Body            string        `json:"body"`
	Title           string        `json:"title"`
	Recipients      []Recipient   `json:"recipients"`
	ExtraRecipients []interface{} `json:"extraRecipients"`
}

// Recipient represents the structure for a recipient in the alert request
type Recipient struct {
	ID   string `json:"id"`
	Type string `json:"type"`
}

func main() {
	// Get environment variables
	apiEndpoint := os.Getenv("DESKALERTS_API_ENDPOINT")
	apiSecretKey := os.Getenv("DESKALERTS_API_KEY")

	if apiEndpoint == "" || apiSecretKey == "" {
		fmt.Println("Error: DESKALERTS_API_ENDPOINT and DESKALERTS_API_KEY environment variables must be set.")
		os.Exit(1)
	}

	// Create a cookie jar to persist cookies across requests
	jar, err := cookiejar.New(nil)
	if err != nil {
		fmt.Printf("Cookie jar creation error: %v\n", err)
		os.Exit(1)
	}

	// Create an HTTP client with the cookie jar
	client := &http.Client{
		Jar: jar,
	}

	// Log in
	fmt.Println("Logging in...")
	loginBody := LoginRequest{Key: apiSecretKey}
	loginJSON, _ := json.Marshal(loginBody)
	loginResp, err := client.Post(apiEndpoint+"account/apikey", "application/json", bytes.NewBuffer(loginJSON))
	if err != nil {
		fmt.Printf("Login error: %v\n", err)
		os.Exit(1)
	}
	defer loginResp.Body.Close()
	fmt.Println("Login Response Status:", loginResp.Status)

	// Search for user "John Doe"
	fmt.Println("Searching for user 'John Doe'...")
	userSearchBody := UserSearchRequest{UserName: "John Doe"}
	userSearchJSON, _ := json.Marshal(userSearchBody)
	userResp, err := client.Post(apiEndpoint+"organization/user", "application/json", bytes.NewBuffer(userSearchJSON))
	if err != nil {
		fmt.Printf("User search error: %v\n", err)
		os.Exit(1)
	}
	defer userResp.Body.Close()

	userRespBody, _ := ioutil.ReadAll(userResp.Body)
	fmt.Println("User Search Response:", string(userRespBody))

	// Parse user ID from response
	var userSearchResp UserSearchResponse
	json.Unmarshal(userRespBody, &userSearchResp)
	userID := userSearchResp.Data.UserID
	if userID == 0 {
		fmt.Println("Failed to retrieve user ID for 'John Doe'")
		os.Exit(1)
	}
	fmt.Println("Found user ID:", userID)

	// Compose and send the alert
	fmt.Println("Sending alert...")
	alertBody := AlertRequest{
		Body:            "This alert was sent through DeskAlerts 11 REST API to a user John Doe",
		Title:           "(Go) DeskAlerts 11 REST API test",
		Recipients:      []Recipient{{ID: fmt.Sprint(userID), Type: "User"}},
		ExtraRecipients: []interface{}{},
	}
	alertJSON, _ := json.Marshal(alertBody)
	alertResp, err := client.Post(apiEndpoint+"publisher/alert/create", "application/json", bytes.NewBuffer(alertJSON))
	if err != nil {
		fmt.Printf("Alert sending error: %v\n", err)
		os.Exit(1)
	}
	defer alertResp.Body.Close()

	alertRespBody, _ := ioutil.ReadAll(alertResp.Body)
	fmt.Println("Alert Creation Response:", string(alertRespBody))
}
