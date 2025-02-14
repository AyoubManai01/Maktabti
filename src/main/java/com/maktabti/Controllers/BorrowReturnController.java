package com.maktabti.Controllers;

import com.maktabti.Services.BookService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class BorrowReturnController {

    @FXML
    private TextField bookIdField; // TextField to input book name
    @FXML
    private Button searchButton; // Search button

    private BookService bookService = new BookService(); // Book service for business logic

    @FXML
    public void initialize() {
        // Set action for the Search button
        searchButton.setOnAction(event -> searchBook());
    }

    // Method to handle the Search button click
    @FXML
    private void searchBook() {
        String bookName = bookIdField.getText().trim(); // Get the book name
        if (bookName.isEmpty()) {
            showErrorPopup("Error", "Please enter a valid book name.");
            return;
        }

        // Fetch book info from Open Library API and check availability
        String bookInfo = bookService.fetchBookDetailsAndCheckAvailability(bookName);

        // Show book information in a popup
        showBookInfoPopup("Book Information", bookInfo);
    }

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
            showSuccessPopup("Success", "The book was borrowed successfully.");
        } else {
            showErrorPopup("Error", "Failed to borrow book. The book may not be available.");
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
            showSuccessPopup("Success", "The book was returned successfully.");
        } else {
            showErrorPopup("Error", "Failed to return book. The book may not exist in the system.");
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

    private void showBookInfoPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Book Details");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

