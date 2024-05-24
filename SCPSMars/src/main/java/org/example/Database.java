package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Database {
    static Connection connection = null;
    static Dotenv dotenv = Dotenv.load();

    public void setup() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lightning","postgres", dotenv.get("password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int numberOfLightningsWeek(LocalDate date) {
        int amount = 0;
        for (int i=0; i<7; i++) {
            amount = amount + numberOfLightningsDay(date.minusDays(i));
        }
        return amount;
    }

    public Map<LocalDate, Integer> fullWeekLightnings(LocalDate lastDay) {
        LocalDate firstDay = lastDay.minusDays(6);
        Map map = new LinkedHashMap();
        for (int i=0; i<7; i++) {
            LocalDate date = firstDay.plusDays(i);
            map.put(date, numberOfLightningsDay(date));
        }
        System.out.println(map);
        return map;
    }

    public int numberOfLightningsDay(LocalDate date) {
        // querry database for amount of lightning on a specific day
        try {
            PreparedStatement querryStatement = connection.prepareStatement("SELECT * FROM lightningdb.day WHERE date = ?");
            querryStatement.setDate(1, Date.valueOf(date));
            ResultSet querryResultSet = querryStatement.executeQuery();

            if (querryResultSet.next()) {
                return querryResultSet.getInt("total_lightnings");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCurrentDayId() {
        try {
            PreparedStatement querryStatement = connection.prepareStatement("SELECT * FROM lightningdb.day WHERE date = ?");
            querryStatement.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet querryResultSet = querryStatement.executeQuery();

            if (querryResultSet.next()) {
                return querryResultSet.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int numberOfLightningsType(int typeId) {
        int dayId = getCurrentDayId();
        // querry database for amount of lightning on a specific day
        try {
            PreparedStatement querryStatement = connection.prepareStatement("SELECT * FROM lightningdb.lightning WHERE day_id = ? AND type_id = ?");
            querryStatement.setInt(1, dayId);
            querryStatement.setInt(2, typeId);
            ResultSet querryResultSet = querryStatement.executeQuery();

            int amount = 0;
            while (querryResultSet.next()) {
                amount++;
            }
            return amount;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int saveDate(LocalDate date) {
        try {
            PreparedStatement querryStatement = connection.prepareStatement("SELECT * FROM lightningdb.day WHERE date = ?");
            querryStatement.setDate(1, Date.valueOf(date));
            ResultSet querryResultSet = querryStatement.executeQuery();

            if (!querryResultSet.next()) {
                // Inserting the new day in the database
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO lightningdb.day (date, total_lightnings) VALUES (?, ?)");
                insertStatement.setDate(1, Date.valueOf(date));
                insertStatement.setInt(2,1);
                insertStatement.execute();
            } else {
                // Updating the amount of lightnings for the already existing day
                int lightnings = querryResultSet.getInt("total_lightnings");
                PreparedStatement insertStatement = connection.prepareStatement("UPDATE lightningdb.day SET total_lightnings = ? WHERE date=?");
                insertStatement.setInt(1,(lightnings+1));
                insertStatement.setDate(2, Date.valueOf(date));
                insertStatement.execute();
            }

            // Returning the id for the given date
            PreparedStatement querryForId = connection.prepareStatement("SELECT * FROM lightningdb.day WHERE date = ?");
            querryForId.setDate(1, Date.valueOf(date));
            ResultSet resultSetDate = querryForId.executeQuery();
            resultSetDate.next();
            return resultSetDate.getInt("id");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertIntoDB(LocalDate date, LocalDateTime localDateTime, int type) {
        // insert lightning into database
        int dayId = saveDate(date);

        try {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO lightningdb.lightning (timestamp, day_id, type_id) VALUES (?,?,?)");
            insertStatement.setTimestamp(1, Timestamp.valueOf(localDateTime));
            insertStatement.setInt(2, dayId);
            insertStatement.setInt(3, type);
            insertStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}