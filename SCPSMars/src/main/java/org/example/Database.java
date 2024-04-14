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
import java.sql.*;

public class Database {
    static Connection connection = null;
    static Dotenv dotenv = Dotenv.load();

    public void setup() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lightning","postgres",dotenv.get("password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void fetch() {

        // querry database for users with cpr x
//        try {
//            PreparedStatement querryStatement = connection.prepareStatement("SELECT * FROM users WHERE cpr = ?");
//            querryStatement.setString(1,"1234123412");
//            ResultSet querryResultSet = querryStatement.executeQuery();
//
//            System.out.println("The following matched the querry:");
//            while (querryResultSet.next()){
//                System.out.println("Name: "+querryResultSet.getString("name")+ " cpr: "+ querryResultSet.getString("cpr"));
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

    }
    public void insertIntoDB(JSONObject lightning) {
        // insert lightning into database

//        try {
//            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO lightningdb.lightning (timestamp, day_id, type_id) VALUES (?,?,?)");
//            insertStatement.setTimestamp(1, lightning);
//            insertStatement.setInt(2, "1234123412");
//            insertStatement.setInt(3, "1234123412");
//            insertStatement.execute();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

}
