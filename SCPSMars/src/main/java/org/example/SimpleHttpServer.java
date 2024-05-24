package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Map;

public class SimpleHttpServer {

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

    static class CloudToCloudHandler implements HttpHandler {
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

    static class CloudToGroundHandler implements HttpHandler {
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


