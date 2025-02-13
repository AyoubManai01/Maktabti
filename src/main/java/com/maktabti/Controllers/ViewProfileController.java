package com.maktabti.Controllers;

import com.maktabti.Utils.CurrentUser;
import com.maktabti.Utils.DBUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ViewProfileController {

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label fineLabel;
    @FXML private Button payFineButton;
    @FXML private Label statusLabel;
    @FXML private ProgressIndicator loadingIndicator;

    private double currentFine = 0.0;

    @FXML
    public void initialize() {
        Stripe.apiKey = "sk_test_51Qs9toP9uimmhvfxF8QUaSCK22cu6Eb21n02sBNJxoQR9pHcnB0S1Nd42650w4cFmXGLcvYU1496HUTlqT0Cdcro00JPxrVV6H";  // Replace with your Stripe secret key
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
                currentFine = fineRs.getDouble("fine");
                fineLabel.setText("$" + currentFine);
                payFineButton.setDisable(currentFine == 0);
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
        if (currentFine <= 0) return;

        loadingIndicator.setVisible(true);
        statusLabel.setText("");
        payFineButton.setText("Processing...");

        try {
            // Create a Checkout Session with Stripe
            Map<String, Object> params = new HashMap<>();
            params.put("success_url", "http://localhost:8080/success");  // URL after successful payment
            params.put("cancel_url", "http://localhost:8080/cancel");    // URL if payment is canceled
            params.put("mode", "payment");
            params.put("payment_method_types", new String[]{"card"});

            // Add line item for the fine amount
            Map<String, Object> lineItem = new HashMap<>();
            lineItem.put("price_data", Map.of(
                    "currency", "usd",
                    "product_data", Map.of("name", "Fine Payment"),
                    "unit_amount", (int) (currentFine * 100)  // Convert to cents
            ));
            lineItem.put("quantity", 1);

            params.put("line_items", new Map[]{lineItem});

            // Create the Checkout Session
            Session session = Session.create(params);

            // Open the Stripe payment page in the browser
            java.awt.Desktop.getDesktop().browse(new java.net.URI(session.getUrl()));

            // Update UI after payment
            loadingIndicator.setVisible(false);
            payFineButton.setText("Pay Fine");

            // Update fine to zero after successful payment
            updateFineToZero();
            showSuccess("Payment successful! Your fine has been cleared.");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Payment failed: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
            loadingIndicator.setVisible(false);
            payFineButton.setText("Pay Fine");
        }
    }

    private void updateFineToZero() {
        int userId = CurrentUser.getUserId();
        try (Connection conn = DBUtil.getConnection()) {
            String updateFineQuery = "UPDATE subscriptions SET fine = 0 WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateFineQuery);
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                fineLabel.setText("$0.00");
                payFineButton.setDisable(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error! Payment could not be processed.");
        }
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}