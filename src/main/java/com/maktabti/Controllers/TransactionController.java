package com.maktabti.Controllers;

import com.maktabti.Entities.Transaction;
import com.maktabti.Services.TransactionService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Stack;

public class TransactionController {

    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, Integer> idColumn;
    @FXML
    private TableColumn<Transaction, Integer> userIdColumn;
    @FXML
    private TableColumn<Transaction, Integer> bookIdColumn;
    @FXML
    private TableColumn<Transaction, String> dateColumn;
    @FXML
    private TableColumn<Transaction, String> typeColumn;
    @FXML
    private TextField userIdField;
    @FXML
    private TextField bookIdField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private Button addTransactionButton;
    @FXML
    private Button deleteTransactionButton;
    @FXML
    private Button exportCsvButton;
    @FXML
    private Button undoButton;  // Undo Button
    @FXML
    private TextField searchField;

    private TransactionService transactionService = new TransactionService();
    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
    private Stack<Transaction> undoStack = new Stack<>();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        userIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserId()).asObject());
        bookIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBookId()).asObject());
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        ));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        typeComboBox.setItems(FXCollections.observableArrayList("borrow", "return"));

        // When the table selection changes, enable the delete button
        transactionTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> deleteTransactionButton.setDisable(newValue == null)
        );

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int userId = Integer.parseInt(newValue);
                updateTable(userId);
            } catch (NumberFormatException e) {
                refreshTable(); // Reset table when input is invalid
            }
        });

        refreshTable(); // Initial load of all data
        updateUndoButton();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void addTransaction() {
        try {
            int userId = Integer.parseInt(userIdField.getText());
            int bookId = Integer.parseInt(bookIdField.getText());
            String type = typeComboBox.getValue();

            if (type == null || type.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a transaction type.");
                return;
            }

            if (!transactionService.bookExists(bookId)) {
                showAlert(Alert.AlertType.ERROR, "Book Not Found", "The book with ID " + bookId + " does not exist in the database.");
                return;
            }

            if (!transactionService.userExists(userId)) {
                showAlert(Alert.AlertType.ERROR, "User Not Found", "The user with ID " + userId + " does not exist in the database.");
                return;
            }

            Transaction transaction = new Transaction(0, userId, bookId, LocalDateTime.now(), type);
            transactionService.addTransaction(transaction);

            // Push the added transaction to the undo stack
            undoStack.push(transaction);

            refreshTable();
            updateUndoButton();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numeric values for User ID and Book ID.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred while adding the transaction.");
        }
    }

    @FXML
    private void deleteTransaction() {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();

        if (selectedTransaction != null) {
            int transactionId = selectedTransaction.getId();
            transactionService.deleteTransaction(transactionId);

            // Push the deleted transaction to the undo stack
            undoStack.push(selectedTransaction);

            refreshTable();
            updateUndoButton();
            showAlert(Alert.AlertType.INFORMATION, "Transaction Deleted", "The transaction has been successfully deleted.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a transaction to delete.");
        }
    }

    // Method to perform undo action
    @FXML
    private void undoTransaction() {
        if (!undoStack.isEmpty()) {
            Transaction lastTransaction = undoStack.pop();

            // Reverse the action based on whether the last transaction was an add or delete
            if (lastTransaction.getId() == 0) {
                transactionService.deleteTransaction(lastTransaction.getId()); // Undo add
            } else {
                transactionService.addTransaction(lastTransaction); // Undo delete
            }

            refreshTable();
            updateUndoButton();
        }
    }

    private void refreshTable() {
        transactionList.clear();
        transactionList.addAll(transactionService.getAllTransactions());
        transactionTable.setItems(transactionList);
    }

    private void updateUndoButton() {
        undoButton.setDisable(undoStack.isEmpty());
    }

    private void updateTable(int userId) {
        transactionList.clear();
        transactionList.addAll(transactionService.searchTransactionsByUserId(userId));
        transactionTable.setItems(transactionList);
    }

    @FXML
    private void exportTransactions() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Transactions");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(exportCsvButton.getScene().getWindow());  // Fixed button reference

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.append("ID,User ID,Book ID,Date,Type\n");
                for (Transaction transaction : transactionList) {
                    writer.append(transaction.getId() + "," +
                            transaction.getUserId() + "," +
                            transaction.getBookId() + "," +
                            transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "," +
                            transaction.getType() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
