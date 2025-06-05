using System.Text;
using System.Text.Json;

class DeskAlertsAPI
{
    private static readonly HttpClient Client = new HttpClient();
    private static readonly string ApiEndpoint = Environment.GetEnvironmentVariable("DESKALERTS_API_ENDPOINT") ?? throw new Exception("DESKALERTS_API_ENDPOINT environment variable not set");
    private static readonly string ApiKey = Environment.GetEnvironmentVariable("DESKALERTS_API_KEY") ?? throw new Exception("DESKALERTS_API_KEY environment variable not set");

    static async Task Main(string[] args)
    {
        // Login 
        await Login();

        // Search for user "John Doe" and get userId
        string userId = await GetUser("John Doe");

        // Compose and send the alert to "John Doe"
        await SendAlert(userId, "This alert was sent through DeskAlerts 11 REST API to a user John Doe", "(C#) DeskAlerts 11 REST API test");
    }

    private static async Task Login()
    {
        var loginData = new
        {
            key = ApiKey
        };

        var content = new StringContent(JsonSerializer.Serialize(loginData), Encoding.UTF8, "application/json");
        var response = await Client.PostAsync(ApiEndpoint + "account/apikey", content);
        response.EnsureSuccessStatusCode();
    }

    private static async Task<string> GetUser(string name)
    {
        var userData = new
        {
            UserName = name
        };
        var content = new StringContent(JsonSerializer.Serialize(userData), Encoding.UTF8, "application/json");
        var response = await Client.PostAsync(ApiEndpoint + "organization/user", content);
        response.EnsureSuccessStatusCode();
        var responseBody = await response.Content.ReadAsStringAsync();
        Console.WriteLine(responseBody);
        var result = JsonSerializer.Deserialize<JsonElement>(responseBody);
        return result.GetProperty("data").GetProperty("userId").ToString();
    }

    private static async Task SendAlert(string userId, string message, string title)
    {
        var alertData = new
        {
            body = message,
            title = title,
            recipients = new[] { new { id = userId, type = "User" } },
            extraRecipients = Array.Empty<object>()
        };
        var content = new StringContent(JsonSerializer.Serialize(alertData), Encoding.UTF8, "application/json");
        var response = await Client.PostAsync(ApiEndpoint + "publisher/alert/create", content);
        response.EnsureSuccessStatusCode();
        var responseBody = await response.Content.ReadAsStringAsync();
        Console.WriteLine("Alert Creation Response: " + responseBody);
    }
}
