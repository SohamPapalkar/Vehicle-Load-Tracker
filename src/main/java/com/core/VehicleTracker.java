package com.core;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class VehicleTracker {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vehicle_system";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                insertVehicleData();
            }
        };

        // Schedule the task to run every 30 minutes for 5 hours
        timer.scheduleAtFixedRate(task, 0, 30 * 60 * 1000);  // 30 minutes in milliseconds

        // Stop the task after 5 hours
        new Timer().schedule(new TimerTask() {
            public void run() {
                timer.cancel();
            }
        }, 5 * 60 * 60 * 1000);  // 5 hours in milliseconds
    }

    private static void insertVehicleData() {
        Random random = new Random();

        // Random data for speed, location, temperature, and load
        String vehicleId = "VH001";
        double speed = 50 + (100 - 50) * random.nextDouble();  // Speed between 50-100 km/h
        String location = "Loc-" + random.nextInt(100);  // Random location
        double temperature = 20 + (40 - 20) * random.nextDouble();  // Temperature between 20-40°C
        double vehicleLoad = 500 + (1000 - 500) * random.nextDouble();  // Load between 500-1000 kg
        double charge = vehicleLoad * 0.1;  // Charge based on load

        String insertSQL = "INSERT INTO vehicle_data (vehicle_id, timestamp, speed, location, temperature, vehicle_load, charge) " +
                           "VALUES (?, NOW(), ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, vehicleId);
            pstmt.setDouble(2, speed);
            pstmt.setString(3, location);
            pstmt.setDouble(4, temperature);
            pstmt.setDouble(5, vehicleLoad);
            pstmt.setDouble(6, charge);

            pstmt.executeUpdate();
            System.out.println("Data inserted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
