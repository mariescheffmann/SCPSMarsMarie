package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
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

    public static void main(String[] args) {
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
        }
    }
}