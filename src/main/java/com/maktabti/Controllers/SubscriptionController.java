package com.maktabti.Controllers;

import com.maktabti.Entities.Subscription;
import com.maktabti.Entities.Transaction;
import com.maktabti.Services.SubscriptionService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SubscriptionController {

    @FXML private TableView<Subscription> subscriptionTable;
    @FXML private TableColumn<Subscription, String> emailColumn;
    @FXML private TableColumn<Subscription, String> startDateColumn;
    @FXML private TableColumn<Subscription, String> endDateColumn;
    @FXML private TableColumn<Subscription, String> fineColumn;
    @FXML private TableColumn<Subscription, Void> historyColumn;
    @FXML private TableColumn<Subscription, Void> deleteColumn;

    @FXML private TextField emailField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField fineField;

    private SubscriptionService subscriptionService;
    private ObservableList<Subscription> subscriptionList;

    public SubscriptionController() throws SQLException {
        subscriptionService = new SubscriptionService();
        subscriptionList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Initialize table columns with appropriate cell values
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        startDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDate().toString()));
        endDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDate().toString()));
        fineColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getFine())));

        // Set up history column to include a button for downloading the history
        historyColumn.setCellFactory(col -> {
            return new TableCell<Subscription, Void>() {
                private final Button downloadButton = new Button("Download History");

                {
                    downloadButton.setOnAction(event -> {
                        Subscription subscription = getTableView().getItems().get(getIndex());
                        exportTransactionHistoryToExcel(subscription.getEmail());
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
            };
        });

        // Set up delete column with a button to delete the subscription
        deleteColumn.setCellFactory(col -> {
            return new TableCell<Subscription, Void>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        Subscription subscription = getTableView().getItems().get(getIndex());
                        deleteSubscription(subscription);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
        });

        // Load the subscription data
        loadSubscriptions();
    }

    // Load all subscriptions from the database
    private void loadSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        subscriptionList.clear();
        subscriptionList.addAll(subscriptions);
        subscriptionTable.setItems(subscriptionList);
    }

    @FXML
    public void addSubscription() {
        String email = emailField.getText();
        String startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : null;
        String endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().toString() : null;
        double fine = fineField.getText().isEmpty() ? 0 : Double.parseDouble(fineField.getText());

        if (email.isEmpty() || startDate == null || endDate == null) {
            // Show an alert if any required field is empty
            showAlert("Error", "Please fill all the fields correctly.");
            return;
        }

        // Add the subscription
        Subscription newSubscription = new Subscription(0, email, LocalDate.parse(startDate), LocalDate.parse(endDate), fine);
        subscriptionService.addSubscription(newSubscription);
        loadSubscriptions(); // Refresh the table with the updated list
    }

    @FXML
    public void deleteSubscription(Subscription subscription) {
        boolean success = subscriptionService.removeSubscription(subscription.getId());
        if (success) {
            subscriptionList.remove(subscription); // Remove from the table view
            showAlert("Success", "Subscription deleted successfully.");
        } else {
            showAlert("Error", "Failed to delete the subscription.");
        }
    }

    // Show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Filter subscriptions by email when typing in the email field
    @FXML
    public void filterSubscriptions(KeyEvent keyEvent) {
        String filterText = emailField.getText();
        if (filterText.isEmpty()) {
            subscriptionTable.setItems(subscriptionList);
        } else {
            ObservableList<Subscription> filteredList = FXCollections.observableArrayList();
            for (Subscription subscription : subscriptionList) {
                if (subscription.getEmail().contains(filterText)) {
                    filteredList.add(subscription);
                }
            }
            subscriptionTable.setItems(filteredList);
        }
    }

    // Export transaction history for a user to an Excel file
    private void exportTransactionHistoryToExcel(String email) {
        try {
            List<Transaction> transactions = subscriptionService.getTransactionsByEmail(email);

            // Create an Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Transaction History");

            // Create headers
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Transaction ID");
            headerRow.createCell(1).setCellValue("User ID");
            headerRow.createCell(2).setCellValue("Book ID");
            headerRow.createCell(3).setCellValue("Transaction Date");
            headerRow.createCell(4).setCellValue("Type");

            // Fill rows with transaction data
            int rowNum = 1;
            for (Transaction transaction : transactions) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(transaction.getId());
                row.createCell(1).setCellValue(transaction.getUserId());
                row.createCell(2).setCellValue(transaction.getBookId());
                row.createCell(3).setCellValue(transaction.getTransactionDate().toString());
                row.createCell(4).setCellValue(transaction.getType());
            }

            // Write the workbook to a file
            try (FileOutputStream fileOut = new FileOutputStream("Transaction_History_" + email + ".xlsx")) {
                workbook.write(fileOut);
            }

            // Show success alert
            showAlert("Success", "Transaction history exported to Excel.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to export transaction history.");
        }
    }
}
