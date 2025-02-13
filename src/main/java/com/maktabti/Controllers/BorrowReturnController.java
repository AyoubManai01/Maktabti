package com.maktabti.Controllers;

import com.maktabti.Services.BookService;
import com.maktabti.Services.TransactionService;
import com.maktabti.Entities.Transaction;
import com.maktabti.Utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDateTime;

public class BorrowReturnController {
    @FXML private TextField bookIdField;
    @FXML private Label messageLabel;

    private BookService bookService = new BookService();
    private TransactionService transactionService = new TransactionService();

    @FXML
    public void borrowBook() {
        try {
            int bookId = Integer.parseInt(bookIdField.getText());
            boolean success = bookService.borrowBook(bookId);
            if(success) {
                messageLabel.setText("Book borrowed successfully.");
                // Record the transaction (assuming the current user is a client)
                int clientId = Session.getCurrentUser().getId();
                Transaction transaction = new Transaction(0, clientId, bookId, LocalDateTime.now(), "borrow");
                transactionService.addTransaction(transaction);
            } else {
                messageLabel.setText("Failed to borrow book. Check availability.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid Book ID.");
        }
    }

    @FXML
    public void returnBook() {
        try {
            int bookId = Integer.parseInt(bookIdField.getText());
            boolean success = bookService.returnBook(bookId);
            if(success) {
                messageLabel.setText("Book returned successfully.");
                // Record the transaction for returning
                int clientId = Session.getCurrentUser().getId();
                Transaction transaction = new Transaction(0, clientId, bookId, LocalDateTime.now(), "return");
                transactionService.addTransaction(transaction);
            } else {
                messageLabel.setText("Failed to return book. Check Book ID.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid Book ID.");
        }
    }
}