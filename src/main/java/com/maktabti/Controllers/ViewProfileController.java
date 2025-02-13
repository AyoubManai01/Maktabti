package com.maktabti.Controllers;

import com.maktabti.Utils.CurrentUser;
import com.maktabti.Utils.DBUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewProfileController {
    @FXML private Label emailLabel;
    @FXML private Label subscriptionEndLabel;
    @FXML private Label fineLabel;
    @FXML private Button payFineButton;

    @FXML
    public void initialize() {
        loadProfile();
    }

    private void loadProfile() {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT u.email, s.end_date, s.fine FROM users u JOIN subscriptions s ON u.id = s.user_id WHERE u.id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, CurrentUser.getUserId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                emailLabel.setText("Email: " + rs.getString("email"));
                subscriptionEndLabel.setText("Subscription Ends: " + rs.getString("end_date"));
                fineLabel.setText("Fine: $" + rs.getDouble("fine"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void payFine() {
        try (Connection conn = DBUtil.getConnection()) {
            String updateQuery = "UPDATE subscriptions SET fine = 0 WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setInt(1, CurrentUser.getUserId());
            stmt.executeUpdate();
            loadProfile(); // Refresh the profile after paying fine
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
