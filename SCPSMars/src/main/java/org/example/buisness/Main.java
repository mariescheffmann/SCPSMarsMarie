package org.example.buisness;

import com.sun.net.httpserver.HttpServer;
import org.example.buisness.SimpleHttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

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

