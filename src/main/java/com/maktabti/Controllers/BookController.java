package com.maktabti.Controllers;

import com.maktabti.Entities.Book;
import com.maktabti.Services.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookController {
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, Integer> copiesColumn;
    @FXML private TextField searchField;

    // Fields for adding a new book (Admin only)
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField isbnField;
    @FXML private TextField copiesField;
    @FXML private TextField searchField1;
    @FXML private Button searchButton;
    @FXML private ListView<String> resultsList;

    private final BookService bookService = new BookService();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (idColumn != null && titleColumn != null && authorColumn != null && isbnColumn != null && copiesColumn != null) {
            idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            titleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
            authorColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAuthor()));
            isbnColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIsbn()));
            copiesColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAvailableCopies()).asObject());
            refreshTable();
        }

        if (searchButton != null && searchField1 != null && resultsList != null) {
            searchButton.setOnAction(e -> searchGoogleBooks());
        }
    }

    private void refreshTable() {
        bookList.setAll(bookService.getAllBooks());
        bookTable.setItems(bookList);
    }

    @FXML
    public void searchBooks() {
        String query = searchField.getText().trim().toLowerCase();
        bookList.setAll(bookService.getAllBooks().stream().filter(b ->
                b.getTitle().toLowerCase().contains(query) ||
                        b.getAuthor().toLowerCase().contains(query) ||
                        b.getIsbn().toLowerCase().contains(query)
        ).toList());
        bookTable.setItems(bookList);
    }

    @FXML
    public void removeBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected != null && bookService.removeBook(selected.getId())) {
            refreshTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to remove book.");
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

            bookService.addBook(new Book(0, title, author, isbn, availableCopies));
            titleField.clear();
            authorField.clear();
            isbnField.clear();
            copiesField.clear();
            refreshTable();
            showAlert(Alert.AlertType.INFORMATION, "Book added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid number for available copies.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message, ButtonType.OK).showAndWait();
    }

    @FXML
    private void searchGoogleBooks() {
        String query = searchField1.getText().trim();
        if (!query.isEmpty()) {
            resultsList.getItems().setAll(fetchGoogleBooks(query));
        }
    }

    private List<String> fetchGoogleBooks(String query) {
        List<String> titles = new ArrayList<>();
        try {
            String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query.replace(" ", "+");
            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setRequestMethod("GET");
            try (Reader reader = new InputStreamReader(conn.getInputStream()); Scanner scanner = new Scanner(reader)) {
                String response = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray items = jsonResponse.optJSONArray("items");
                if (items != null) {
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");
                        titles.add(volumeInfo.optString("title", "Unknown Title"));
                    }
                }
            }
        } catch (Exception e) {
            titles.add("Error fetching data");
        }
        return titles;
    }

    @FXML
    private void openGoogleBooksSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GoogleBooksSearch.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Google Books Search");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
