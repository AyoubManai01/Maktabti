package com.maktabti.Controllers;

import com.maktabti.Services.BookService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BorrowReturnController {
    @FXML private TextField bookIdField;
    @FXML private Label messageLabel;

    private BookService bookService = new BookService();

    @FXML
    public void borrowBook() {
        try {
            int bookId = Integer.parseInt(bookIdField.getText());
            boolean success = bookService.borrowBook(bookId);
            if(success) {
                messageLabel.setText("Book borrowed successfully.");
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
            } else {
                messageLabel.setText("Failed to return book. Check Book ID.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid Book ID.");
        }
    }
}
