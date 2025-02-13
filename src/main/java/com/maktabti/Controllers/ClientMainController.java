package com.maktabti.Controllers;

import com.maktabti.Utils.CurrentUser;
import com.maktabti.Utils.DBUtil;
import javafx.event.ActionEvent;
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
    @FXML private StackPane contentPane; // This must be correctly linked in FXML

    @FXML
    public void initialize() {
        if (contentPane == null) {
            System.out.println("ERROR: contentPane is NULL! Check ClientMain.fxml.");
        } else {
            System.out.println("✅ contentPane is initialized correctly.");
            showBooks(); // Load books page by default
        }
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
            Scene scene = new Scene(root, 1024, 768);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadContent(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent content = loader.load();

            if (contentPane == null) {
                System.out.println("ERROR: contentPane is NULL in loadContent(). Check FXML.");
                return;
            }

            contentPane.getChildren().clear();
            contentPane.getChildren().add(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
