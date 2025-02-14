package com.maktabti.Controllers;

import com.maktabti.Utils.CurrentUser;
import com.maktabti.Utils.DBUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientMainController {
    @FXML private StackPane contentPane;

    @FXML
    public void initialize() {
        showBooks();
    }

    @FXML public void showBooks() {
        loadContent("/BookPane.fxml");
    }

    @FXML public void showBorrowReturn() {
        loadContent("/BorrowReturnPane.fxml");
    }

    @FXML public void showAccount() {
        loadContent("/ViewProfile.fxml");
    }

    @FXML public void logout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) contentPane.getScene().getWindow();
            stage.setScene(new Scene(root, 1024, 768));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadContent(String fxml) {
        try {
            Parent content = FXMLLoader.load(getClass().getResource(fxml));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProfile() {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT email FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, CurrentUser.getUserId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String email = rs.getString("email");
                Label emailLabel = new Label("Email: " + email);

                VBox profilePane = new VBox(10, emailLabel);
                profilePane.setStyle("-fx-padding: 20px;");
                contentPane.getChildren().clear();
                contentPane.getChildren().add(profilePane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
