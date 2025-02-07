package com.maktabti.Controllers;

import com.maktabti.Entities.Subscription;
import com.maktabti.Services.SubscriptionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.util.Optional;

public class SubscriptionController {
    public Button addSubscriptionButton;
    @FXML
    private TableView<Subscription> subscriptionTable;
    @FXML
    private TableColumn<Subscription, Integer> idColumn;
    @FXML
    private TableColumn<Subscription, Integer> userIdColumn;
    @FXML
    private TableColumn<Subscription, String> startDateColumn;
    @FXML
    private TableColumn<Subscription, String> endDateColumn;
    @FXML
    private TableColumn<Subscription, Double> fineColumn;
    @FXML
    private TextField userIdField;
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
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        userIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getUserId()).asObject());
        startDateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStartDate().toString()));
        endDateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEndDate().toString()));
        fineColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getFine()).asObject());

        refreshTable();
    }

    @FXML
    private void addSubscription() {
        if (validateInput()) {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate.isAfter(endDate)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Dates", "Start date must be earlier than end date.", "");
                return;
            }

            int userId = Integer.parseInt(userIdField.getText());
            double fine = Double.parseDouble(fineField.getText());

            Subscription subscription = new Subscription(0, userId, startDate, endDate, fine);
            subscriptionService.addSubscription(subscription);
            refreshTable();
            clearFields();
        }
    }


    @FXML
    private void deleteSubscription() {
        Subscription selectedSubscription = subscriptionTable.getSelectionModel().getSelectedItem();
        if (selectedSubscription != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Subscription");
            alert.setContentText("Are you sure you want to delete this subscription?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                subscriptionService.removeSubscription(selectedSubscription.getId());
                refreshTable();
            }
        } else {
            showAlert(AlertType.WARNING, "No Selection", "No Subscription Selected", "Please select a subscription in the table.");
        }
    }

    private void refreshTable() {
        subscriptionList.clear();
        subscriptionList.addAll(subscriptionService.getAllSubscriptions());
        subscriptionTable.setItems(subscriptionList);
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (userIdField.getText() == null || userIdField.getText().isEmpty()) {
            errorMessage += "User ID is required.\n";
        } else {
            try {
                Integer.parseInt(userIdField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "User ID must be an integer.\n";
            }
        }

        if (startDatePicker.getValue() == null) {
            errorMessage += "Start date is required.\n";
        }

        if (endDatePicker.getValue() == null) {
            errorMessage += "End date is required.\n";
        }

        if (fineField.getText() == null || fineField.getText().isEmpty()) {
            errorMessage += "Fine is required.\n";
        } else {
            try {
                Double.parseDouble(fineField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Fine must be a valid number.\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(AlertType.ERROR, "Invalid Input", "Please correct the following errors:", errorMessage);
            return false;
        }
    }

    private void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        userIdField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        fineField.clear();
    }
}
