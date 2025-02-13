package com.maktabti.Controllers;

import com.maktabti.Entities.Transaction;
import com.maktabti.Services.TransactionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

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
    private TextField searchField;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private Button addTransactionButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button exportButton;

    private TransactionService transactionService = new TransactionService();
    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        userIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getUserId()).asObject());
        bookIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getBookId()).asObject());
        dateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTransactionDate().toString()));
        typeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));
        typeComboBox.setItems(FXCollections.observableArrayList("borrow", "return"));
        filterComboBox.setItems(FXCollections.observableArrayList("User ID", "Book ID", "Type", "Date"));
        refreshTable();

        // Highlight overdue transactions
        transactionTable.setRowFactory(tv -> new TableRow<Transaction>() {
            @Override
            protected void updateItem(Transaction transaction, boolean empty) {
                super.updateItem(transaction, empty);
                if (transaction == null || empty) {
                    setStyle("");
                } else if ("borrow".equals(transaction.getType()) && transaction.getTransactionDate().plusDays(14).isBefore(LocalDateTime.now())) {
                    setStyle("-fx-background-color: #ffcccc;"); // Red for overdue
                } else {
                    setStyle("");
                }
            }
        });
    }

    @FXML
    private void addTransaction() {
        int userId = Integer.parseInt(userIdField.getText());
        int bookId = Integer.parseInt(bookIdField.getText());
        String type = typeComboBox.getValue();
        Transaction transaction = new Transaction(0, userId, bookId, LocalDateTime.now(), type);
        transactionService.addTransaction(transaction);
        refreshTable();
    }

    @FXML
    private void searchTransactions() {
        String query = searchField.getText();
        String filter = filterComboBox.getValue();

        List<Transaction> filteredTransactions = transactionService.searchTransactions(filter, query);
        transactionList.setAll(filteredTransactions);
        transactionTable.setItems(transactionList);
    }

    @FXML
    private void exportTransactions() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Transactions");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("ID,User ID,Book ID,Date,Type");
                for (Transaction transaction : transactionList) {
                    writer.println(transaction.getId() + "," +
                            transaction.getUserId() + "," +
                            transaction.getBookId() + "," +
                            transaction.getTransactionDate() + "," +
                            transaction.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshTable() {
        transactionList.clear();
        transactionList.addAll(transactionService.getAllTransactions());
        transactionTable.setItems(transactionList);
    }
}
