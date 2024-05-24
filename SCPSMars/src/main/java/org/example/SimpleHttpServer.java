package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class SimpleHttpServer {
    static Dotenv dotenv = Dotenv.load();
    private static String API_PARAMETERS = "period=latest-10-minutes&";
    private static String API = String.format("https://dmigw.govcloud.dk/v2/lightningdata/collections/observation/items?%sapi-key=%s", API_PARAMETERS, dotenv.get("API_KEY"));

    static class tenMinuteHandler implements HttpHandler {
        @Override
        public void handle (HttpExchange exchange) throws IOException {
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

                db.insertIntoDB(date, dateTime, (type+1));
            }

            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            setCorsHeaders(exchange);

            String returnValue = Integer.toString(jsonArray.size());
            exchange.sendResponseHeaders(200, returnValue.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(returnValue.getBytes());
            os.close();
        }
    }

    static class TodayHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set response headers
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            setCorsHeaders(exchange);

            // Get the number for today
            Database db = new Database();
            int numberToday = db.numberOfLightningsDay(LocalDate.now());

            // Send response
            String response = Integer.toString(numberToday);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class ThisWeekHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set response headers
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            setCorsHeaders(exchange);

            // Get the number for yesterday
            Database db = new Database();
            int numberLightningsWeek = db.numberOfLightningsWeek(LocalDate.now());

            // Send response
            String response = Integer.toString(numberLightningsWeek);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class CloudToGroundHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set response headers
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            setCorsHeaders(exchange);

            // Get the number for yesterday
            Database db = new Database();
            int numberLightningsType1 = db.numberOfLightningsType(1);
            int numberLightningsType2 = db.numberOfLightningsType(2);
            int numberOfLightningsCloud = numberLightningsType1 + numberLightningsType2;

            // Send response
            String response = Integer.toString(numberOfLightningsCloud);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class CloudToCloudHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set response headers
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            setCorsHeaders(exchange);

            // Get the number for yesterday
            Database db = new Database();
            int numberOfLightningsCloud = db.numberOfLightningsType(3);

            // Send response
            String response = Integer.toString(numberOfLightningsCloud);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class FullWeekHandeler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set response headers
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            setCorsHeaders(exchange);

            // Get data for a full week
            Database db = new Database();
            Map fullWeekMap = db.fullWeekLightnings(LocalDate.now());

            // Send response
            String response = fullWeekMap.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:8080");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
    }
}


