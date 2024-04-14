package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    static Dotenv dotenv = Dotenv.load();
    private static String API_PARAMETERS = "period=latest-hour&";
    private static String API = String.format("https://dmigw.govcloud.dk/v2/lightningdata/collections/observation/items?%sapi-key=%s", API_PARAMETERS, dotenv.get("API_KEY"));

    public static void main(String[] args) {
        //polling

        //hive data fra lightningInstance ud og dele det op i de kategorier der skal bruges i databasen
        JSONObject lightning = lightningInstance();

    }
    public static JSONObject lightningInstance () {
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

        JsonObject j = new JsonParser().parse(response.body()).getAsJsonObject();
        JSONObject lightning = new JSONObject(j.asMap());

        System.out.println(lightning.get("numberReturned"));
        return lightning;
    }
}