package com.maktabti.Controllers;

import com.maktabti.Entities.Subscription;
import com.maktabti.Services.SubscriptionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SubscriptionController {
    public Button addSubscriptionButton;
    @FXML
    private TableView<Subscription> subscriptionTable;
    @FXML
    private TableColumn<Subscription, String> emailColumn;
    @FXML
    private TableColumn<Subscription, String> startDateColumn;
    @FXML
    private TableColumn<Subscription, String> endDateColumn;
    @FXML
    private TableColumn<Subscription, Double> fineColumn;
    @FXML
    private TableColumn<Subscription, Void> historyColumn;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField fineField;

    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final ObservableList<Subscription> subscriptionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        startDateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStartDate().toString()));
        endDateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEndDate().toString()));
        fineColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getFine()).asObject());

        historyColumn.setCellFactory(param -> new TableCell<>() {
            private final Button downloadButton = new Button("Download History");

            {
                downloadButton.setOnAction(event -> {
                    Subscription subscription = getTableView().getItems().get(getIndex());
                    downloadBorrowHistory(subscription.getEmail());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(downloadButton);
                }
            }
        });

        refreshTable();
    }

    @FXML
    private void addSubscription() {
        if (validateInput()) {
            Subscription subscription = new Subscription(0, emailField.getText(), startDatePicker.getValue(), endDatePicker.getValue(), Double.parseDouble(fineField.getText()));
            subscriptionService.addSubscription(subscription);
            refreshTable();
            clearFields();
        }
    }

    private void downloadBorrowHistory(String email) {
        List<String[]> history = subscriptionService.getBorrowHistory(email);
        if (history.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No History", "No borrow history available for this user.", "");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Borrow History");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName(email + "_borrow_history.csv");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.append("Borrow Date, Book Title, Return Date\n");
                for (String[] entry : history) {
                    writer.append(String.join(",", entry)).append("\n");
                }
                showAlert(Alert.AlertType.INFORMATION, "Download Complete", "Borrow history downloaded successfully.", "");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "File Error", "Failed to save file.", e.getMessage());
            }
        }
    }

    private void refreshTable() {
        subscriptionList.clear();
        subscriptionList.addAll(subscriptionService.getAllSubscriptions());
        subscriptionTable.setItems(subscriptionList);
    }

    private boolean validateInput() {
        if (emailField.getText().isEmpty() || startDatePicker.getValue() == null || endDatePicker.getValue() == null || fineField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "All fields must be filled.", "");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        emailField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        fineField.clear();
    }
}
