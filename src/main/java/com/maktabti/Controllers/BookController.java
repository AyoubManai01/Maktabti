package com.maktabti.Controllers;

import com.maktabti.Entities.Book;
import com.maktabti.Services.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BookController {
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, Integer> copiesColumn;
    @FXML private TextField searchField;

    // Admin-only fields for adding a new book.
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField copiesField;
    @FXML
    private TextField searchFieldA;

    private BookService bookService = new BookService();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        titleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        authorColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAuthor()));
        isbnColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIsbn()));
        copiesColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAvailableCopies()).asObject());
        refreshTable();
    }

    private void refreshTable() {
        bookList.clear();
        bookList.addAll(bookService.getAllBooks());
        bookTable.setItems(bookList);
    }

    @FXML
    public void searchBooks() {
        String query = searchField.getText().trim().toLowerCase();
        bookList.clear();
        bookList.addAll(bookService.getAllBooks().stream().filter(b ->
                b.getTitle().toLowerCase().contains(query) ||
                        b.getAuthor().toLowerCase().contains(query) ||
                        b.getIsbn().toLowerCase().contains(query)
        ).toList());
        bookTable.setItems(bookList);
    }

    @FXML
    public void removeBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if(selected != null) {
            if(bookService.removeBook(selected.getId())) {
                refreshTable();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to remove book.");
                alert.show();
            }
        }
    }
    @FXML
    public void addBook() {
        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            int availableCopies = Integer.parseInt(copiesField.getText().trim());

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please fill all fields.");
                return;
            }

            Book newBook = new Book(0, title, author, isbn, availableCopies);
            bookService.addBook(newBook);

            titleField.clear();
            authorField.clear();
            isbnField.clear();
            copiesField.clear();

            refreshTable();
            showAlert(Alert.AlertType.INFORMATION, "Book added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid number for available copies.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed to add book. Please try again.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.showAndWait();
    }

}
