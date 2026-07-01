import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PriorityInbox {

    public static void main(String[] args) {

        String apiUrl = "http://4.224.186.213/evaluation-service/notifications";

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiIyMzExaXQwMTAwNjFAbWFsbGFyZWRkeXVuaXZlcnNpdHkuYWMuaW4iLCJleHAiOjE3ODI4ODc4MDMsImlhdCI6MTc4Mjg4NjkwMywiaXNzIjoiQWZmb3JkIE1lZGljYWwgVGVjaG5vbG9naWVzIFByaXZhdGUgTGltaXRlZCIsImp0aSI6IjY3YWI1NzU5LTk2ZmMtNDVjNC1hOTU5LWQwYzQ5MWUwYTA3NiIsImxvY2FsZSI6ImVuLUlOIiwibmFtZSI6InZlbnUgZ29wYWwgZ2FkZGFtaWRpIiwic3ViIjoiMmQzMTgwY2ItZWQ3Zi00ZTk4LTgzMjQtNzBjZTRjZjdiOTdiIn0sImVtYWlsIjoiMjMxMWl0MDEwMDYxQG1hbGxhcmVkZHl1bml2ZXJzaXR5LmFjLmluIiwibmFtZSI6InZlbnUgZ29wYWwgZ2FkZGFtaWRpIiwicm9sbE5vIjoiMjMxMWl0MDEwMDYxIiwiYWNjZXNzQ29kZSI6InhwUWRkZCIsImNsaWVudElEIjoiMmQzMTgwY2ItZWQ3Zi00ZTk4LTgzMjQtNzBjZTRjZjdiOTdiIiwiY2xpZW50U2VjcmV0IjoiRWRuZGpVY1FxalZuY2VDeSJ9.RK0sP2fVYwH4U8qhbrqpnRJyYXpdZBapNc9Uf7CD2XI";

        try {

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("Status Code: " + response.statusCode());
            System.out.println(response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}