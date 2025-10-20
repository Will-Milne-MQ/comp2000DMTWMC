import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://13.238.167.130/weather"))
                .header("Accept", "text/event-stream")
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(HttpResponse::body)
                .thenAccept(inputStream -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        //String line;
                        reader.lines()
                            .forEach(line -> System.out.println(line));
                        // while ((line = reader.readLine()) != null) {
                        //     System.out.print("Received: " + line);
                        //     if(line.contains("rain")){
                        //         System.out.print(" Rain is at third value for the grid location first value, second value");
                        //     }
                        //     else if(line.contains("windx")){
                        //         System.out.print(" Windx is at third value for the grid location first value, second value");
                        //     }
                        //     else if(line.contains("windy")){
                        //         System.out.print(" Windy is at third value for the grid location first value, second value");
                        //     }
                        //     else if(line.contains("temp")){
                        //         System.out.print(" temp is at third value for the grid location first value, second value");
                        //     }
                        //     System.out.println();
                        // }
                    } catch (IOException e) {
                        System.err.println("Error reading Server Side Event (SSE) stream: " + e.getMessage());
                    }
                })
                .join(); // Wait for the async operation to complete
    }
}