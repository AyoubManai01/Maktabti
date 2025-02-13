package com.maktabti.Controllers;

import com.maktabti.Services.BookService;
import com.maktabti.Services.TransactionService;
import com.maktabti.Entities.Transaction;
import com.maktabti.Utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

public class BorrowReturnController {

    @FXML
    private TextField bookIdField; // TextField to input book name

    private BookService bookService = new BookService(); // Book service for business logic
    private TransactionService transactionService = new TransactionService(); // Transaction service for recording actions

    @FXML
    public void borrowBook() {
        String bookName = bookIdField.getText().trim(); // Get the book name
        if (bookName.isEmpty()) {
            showErrorPopup("Error", "Please enter a valid book name.");
            return;
        }

        int bookId = bookService.getBookIdByName(bookName); // Retrieve the book ID
        if (bookId == -1) {
            showErrorPopup("Error", "Book not found. Check the book name.");
            return;
        }

        boolean success = bookService.borrowBookByName(bookName); // Borrow the book
        if (success) {
            showSuccessPopup("Success", "The book was borrowed successfully."); // Show success pop-up
            int clientId = Session.getCurrentUser().getId();
            Transaction transaction = new Transaction(0, clientId, bookId, LocalDateTime.now(), "borrow");
            transactionService.addTransaction(transaction); // Record transaction
        } else {
            showErrorPopup("Error", "Failed to borrow book. Check availability.");
        }
    }

    @FXML
    public void returnBook() {
        String bookName = bookIdField.getText().trim(); // Get the book name
        if (bookName.isEmpty()) {
            showErrorPopup("Error", "Please enter a valid book name.");
            return;
        }

        int bookId = bookService.getBookIdByName(bookName); // Retrieve the book ID
        if (bookId == -1) {
            showErrorPopup("Error", "Book not found. Check the book name.");
            return;
        }

        boolean success = bookService.returnBookByName(bookName); // Return the book
        if (success) {
            showSuccessPopup("Success", "The book was returned successfully."); // Show success pop-up
            int clientId = Session.getCurrentUser().getId();
            Transaction transaction = new Transaction(0, clientId, bookId, LocalDateTime.now(), "return");
            transactionService.addTransaction(transaction); // Record transaction
        } else {
            showErrorPopup("Error", "Failed to return book. Check the book name.");
        }
    }

    private void showErrorPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
