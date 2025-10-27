import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://13.238.167.130/rockyou"))
                .header("Accept", "text/event-stream")
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(HttpResponse::body)
                .thenAccept(inputStream -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        String line;
                        Map<Integer, List<String>> lines = reader.lines().limit(10).collect(Collectors.groupingBy(String::length, Collectors.mapping(s -> "#", Collectors.toList())));
                        // int i = 0;
                        // while ((line = reader.readLine()) != null) {
                        //     System.out.println("Recieved: " + line);
                        //     // System.out.println(i);
                        //     // i++;
                        // }
                        for(Integer key: lines.keySet()){
                            System.out.println(Integer.toString(key) + ": " + lines.get(key).stream().collect(Collectors.joining()));
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading Server Side Event (SSE) stream: " + e.getMessage());
                    }
                })
                .join(); // Wait for the async operation to complete
    }
}