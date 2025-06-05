import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.net.CookieManager;
import java.net.CookiePolicy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.github.cdimascio.dotenv.Dotenv;

public class SendAlert {
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
            .build();
    
    // Load environment variables from .env file if present, otherwise from system environment
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final String API_ENDPOINT = getRequiredEnv("API_ENDPOINT");
    private static final String API_KEY = getRequiredEnv("API_KEY");
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // Helper method to get environment variables and throw an exception if not set
    private static String getRequiredEnv(String key) {
        String value = dotenv.get(key);
        if (value == null || value.isEmpty()) {
            value = System.getenv(key);
        }
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException("Required environment variable " + key + " is not set.");
        }
        return value;
    }
    
    public static void main(String[] args) {
        try {
            // Login
            login();
            
            // Search for user "John Doe" and get userId
            String userId = getUser("John Doe");
            
            // Compose and send the alert to "John Doe"
            sendAlert(userId, "(JAVA) This alert was sent through DeskAlerts 11 REST API to a user John Doe", "DeskAlerts 11 REST API test");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void login() throws IOException, InterruptedException {
        ObjectNode loginData = objectMapper.createObjectNode();
        loginData.put("key", API_KEY);
        
        String jsonString = objectMapper.writeValueAsString(loginData);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "account/apikey"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("Login failed with status code: " + response.statusCode());
        }
    }
    
    private static String getUser(String name) throws IOException, InterruptedException {
        ObjectNode userData = objectMapper.createObjectNode();
        userData.put("UserName", name);
        
        String jsonString = objectMapper.writeValueAsString(userData);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "organization/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("Get user failed with status code: " + response.statusCode());
        }
        
        String responseBody = response.body();
        System.out.println(responseBody);
        
        JsonNode result = objectMapper.readTree(responseBody);
        return result.get("data").get("userId").asText();
    }
    
    private static void sendAlert(String userId, String message, String title) throws IOException, InterruptedException {
        ObjectNode alertData = objectMapper.createObjectNode();
        alertData.put("body", message);
        alertData.put("title", title);
        ArrayNode extraRecipients = objectMapper.createArrayNode();
        alertData.set("extraRecipients", extraRecipients);
        
        ArrayNode recipients = objectMapper.createArrayNode();
        ObjectNode recipient = objectMapper.createObjectNode();
        recipient.put("id", userId);
        recipient.put("type", "User");
        recipients.add(recipient);
        alertData.set("recipients", recipients);
        
        String jsonString = objectMapper.writeValueAsString(alertData);
        
        // Print JSON request for debugging
        System.out.println("Sending alert JSON: " + jsonString);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + "publisher/alert/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("Send alert failed with status code: " + response.statusCode());
        }
    }
}