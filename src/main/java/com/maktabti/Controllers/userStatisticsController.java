package com.maktabti.Controllers;

import com.maktabti.Services.UserService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.awt.*;

public class userStatisticsController {

    @FXML
    private PieChart pieChart;
    @FXML private Button goBackButton;

    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        JSONObject stats = userService.getUserStatistics();
        int totalUsers = stats.optInt("totalUsers", 0);
        int adminCount = stats.optInt("adminCount", 0);
        int clientCount = stats.optInt("clientCount", 0);

        double adminPercentage = totalUsers > 0 ? (adminCount * 100.0 / totalUsers) : 0;
        double clientPercentage = totalUsers > 0 ? (clientCount * 100.0 / totalUsers) : 0;

        PieChart.Data adminData = new PieChart.Data("Admins (" + String.format("%.1f", adminPercentage) + "%)", adminCount);
        PieChart.Data clientData = new PieChart.Data("Clients (" + String.format("%.1f", clientPercentage) + "%)", clientCount);

        pieChart.setData(FXCollections.observableArrayList(adminData, clientData));
        pieChart.setTitle("User Statistics");
    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AdminMain.fxml"));
            Stage stage = (Stage) goBackButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
