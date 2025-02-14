package com.maktabti.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdminMainController {
    @FXML private StackPane contentPane;

    @FXML
    public void initialize() {
        showBooks(); // Load the table view by default
    }

    @FXML
    public void showBooks() {
        loadContent("/AdminBookPane.fxml");
    }

    @FXML
    public void showUsers() { loadContent("/UserPane.fxml"); }

    @FXML
    public void showTransactions() { loadContent("/TransactionPane.fxml"); }

    @FXML
    public void showSubscriptions() { loadContent("/SubscriptionPane.fxml"); }

    @FXML
    public void showBorrowReturn() { loadContent("/BorrowReturnPane.fxml"); }

    @FXML
    public void openGoogleBooksSearch() { loadContent("/GoogleBooksSearch.fxml"); }

    @FXML
    public void logout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) contentPane.getScene().getWindow();
            Scene scene = new Scene(root, 1024, 768);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void loadContent(String fxml) {
        try {
            Parent content = FXMLLoader.load(getClass().getResource(fxml));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(content);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
