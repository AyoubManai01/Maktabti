package com.maktabti.Controllers;

import com.maktabti.Utils.CurrentUser;
import com.maktabti.Utils.DBUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewProfileController {

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;

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
            String query = "SELECT username, email FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usernameLabel.setText(rs.getString("username"));
                emailLabel.setText(rs.getString("email"));
            } else {
                emailLabel.setText("⚠ User not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            emailLabel.setText("⚠ Database error!");
        }
    }
}
