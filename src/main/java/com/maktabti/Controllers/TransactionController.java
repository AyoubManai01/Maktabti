package com.maktabti.Controllers;

import com.maktabti.Entities.Transaction;
import com.maktabti.Services.TransactionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;

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
        refreshTable();
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

    private void refreshTable() {
        transactionList.clear();
        transactionList.addAll(transactionService.getAllTransactions());
        transactionTable.setItems(transactionList);
    }
}
