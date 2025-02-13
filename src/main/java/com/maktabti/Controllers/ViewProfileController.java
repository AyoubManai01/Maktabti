package com.maktabti.Controllers;

import com.maktabti.Utils.CurrentUser;
import com.maktabti.Utils.DBUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewProfileController {

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label fineLabel;
    @FXML private Button payFineButton;

    @FXML
    public void initialize() {
        loadProfileData();
    }

    private void loadProfileData() {
        int userId = CurrentUser.getUserId();
        if (userId == 0) {
            emailLabel.setText("⚠ No user logged in!");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            // Fetch user info
            String userQuery = "SELECT username, email FROM users WHERE id = ?";
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setInt(1, userId);
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                usernameLabel.setText(userRs.getString("username"));
                emailLabel.setText(userRs.getString("email"));
            } else {
                emailLabel.setText("⚠ User not found!");
                return;
            }

            // Fetch fine from subscriptions table
            String fineQuery = "SELECT fine FROM subscriptions WHERE user_id = ?";
            PreparedStatement fineStmt = conn.prepareStatement(fineQuery);
            fineStmt.setInt(1, userId);
            ResultSet fineRs = fineStmt.executeQuery();

            if (fineRs.next()) {
                double fine = fineRs.getDouble("fine");
                fineLabel.setText("$" + fine);
                payFineButton.setDisable(fine == 0);
            } else {
                fineLabel.setText("$0.00");
                payFineButton.setDisable(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            emailLabel.setText("⚠ Database error!");
        }
    }

    @FXML
    private void handlePayFine() {
        int userId = CurrentUser.getUserId();
        if (userId == 0) return;

        try (Connection conn = DBUtil.getConnection()) {
            String updateFineQuery = "UPDATE subscriptions SET fine = 0 WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateFineQuery);
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                fineLabel.setText("$0.00");
                payFineButton.setDisable(true);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment Successful");
                alert.setHeaderText(null);
                alert.setContentText("Your fine has been paid successfully.");
                alert.showAndWait();
            } else {
                showError("Failed to update fine. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error! Payment could not be processed.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
