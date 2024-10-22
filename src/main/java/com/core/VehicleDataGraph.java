package com.core;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.sql.*;

public class VehicleDataGraph {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vehicle_system";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Vehicle Data Graph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(createChartPanel());
            frame.pack();
            frame.setVisible(true);
        });
    }

    private static JPanel createChartPanel() {
        String chartTitle = "Vehicle Data Over Time";
        String categoryAxisLabel = "Time";
        String valueAxisLabel = "Values";

        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createLineChart(chartTitle,
                                                        categoryAxisLabel, valueAxisLabel, dataset,
                                                        PlotOrientation.VERTICAL, true, true, false);

        return new ChartPanel(chart);
    }

    private static DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT timestamp, speed, temperature, vehicle_load FROM vehicle_data WHERE vehicle_id = 'VH001'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String timestamp = rs.getString("timestamp");
                double speed = rs.getDouble("speed");
                double temperature = rs.getDouble("temperature");
                double vehicleLoad = rs.getDouble("vehicle_load");

                dataset.addValue(speed, "Speed", timestamp);
                dataset.addValue(temperature, "Temperature", timestamp);
                dataset.addValue(vehicleLoad, "Vehicle Load", timestamp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataset;
    }
}
