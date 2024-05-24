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
    public static void main(String[] args) throws IOException {
        //System.out.println("Now: " + LocalDate.now());
        //db.numberOfLightningsWeek(LocalDate.now());

        //Setting up server to host backend
        // Create HTTP server listening on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Create a context for "/api/data" endpoint
        server.createContext("/api/tenMinutes", new SimpleHttpServer.tenMinuteHandler());
        server.createContext("/api/day", new SimpleHttpServer.TodayHandler());
        server.createContext("/api/week", new SimpleHttpServer.ThisWeekHandler());
        server.createContext("/api/cloudToCloud", new SimpleHttpServer.CloudToCloudHandler());
        server.createContext("/api/cloudToGround", new SimpleHttpServer.CloudToGroundHandler());
        server.createContext("/api/fullWeek", new SimpleHttpServer.FullWeekHandeler());

        server.setExecutor(null); // Use the default executor

        // Start the server
        server.start();
        System.out.println("Server started on port 8000");
    }
}

