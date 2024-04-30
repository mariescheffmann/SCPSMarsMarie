package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Main {
    static Dotenv dotenv = Dotenv.load();
    private static String API_PARAMETERS = "period=latest-day&";
    private static String API = String.format("https://dmigw.govcloud.dk/v2/lightningdata/collections/observation/items?%sapi-key=%s", API_PARAMETERS, dotenv.get("API_KEY"));

    public static void main(String[] args) throws IOException {
        Database db = new Database();
        db.setup();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("features");

        for (int i=0; i<jsonArray.size(); i++) {
            JsonObject properties = jsonArray.get(i).getAsJsonObject().get("properties").getAsJsonObject();

            int type = properties.get("type").getAsInt();
            String timeString = properties.get("observed").toString().replace("\"", "");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
            LocalDateTime dateTime = LocalDateTime.parse(timeString, formatter);
            LocalDate date = dateTime.toLocalDate();

            db.insertIntoDB(date, dateTime, type);

            System.out.println("Lyn i dag : " + db.numberOfLightningsDay(date));
            System.out.println(LocalDate.now());
            System.out.println(date);

            System.out.println(db.numberOfLightningsType(1));

        }
        //System.out.println("Now: " + LocalDate.now());
        //db.numberOfLightningsWeek(LocalDate.now());

        //Setting up server to host backend
        // Create HTTP server listening on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Create a context for "/api/data" endpoint
        server.createContext("/api/day", new SimpleHttpServer.TodayHandler());
        server.createContext("/api/week", new SimpleHttpServer.ThisWeekHandler());
        server.createContext("/api/cloudToCloud", new SimpleHttpServer.CloudToCloudHandler());
        server.createContext("/api/cloudToGround", new SimpleHttpServer.CloudToGroundHandler());

        // Start the server
        server.start();
        System.out.println("Server started on port 8000");
    }
}

