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
    // ----- Main Window Fields -----
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

    // ----- Google Books Search Popup Fields -----
    @FXML private TextField searchField1;
    @FXML private Button searchButton;
    @FXML private ListView<GoogleBook> resultsList;
    @FXML private Button addGoogleBookButton;

    private final BookService bookService = new BookService();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize the main table view (if present)
        if (idColumn != null && titleColumn != null && authorColumn != null && isbnColumn != null && copiesColumn != null) {
            idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            titleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
            authorColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAuthor()));
            isbnColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIsbn()));
            copiesColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAvailableCopies()).asObject());
            refreshTable();
        }

        // Initialize Google Books search popup elements (if present)
        if (searchField1 != null && searchButton != null && resultsList != null) {
            // When the Search button is clicked, call our google search method.
            searchButton.setOnAction(e -> searchGoogleBooks());

            // Use a custom cell factory to display title, author, and ISBN for each GoogleBook.
            resultsList.setCellFactory(listView -> new ListCell<GoogleBook>() {
                @Override
                protected void updateItem(GoogleBook item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText("Title: " + item.getTitle() + "\nAuthor: " + item.getAuthor() + "\nISBN: " + item.getIsbn());
                    }
                }
            });
        }
        if (addGoogleBookButton != null) {
            addGoogleBookButton.setOnAction(e -> addGoogleBook());
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
            List<GoogleBook> googleBooks = fetchGoogleBooks(query);
            ObservableList<GoogleBook> observableBooks = FXCollections.observableArrayList(googleBooks);
            resultsList.setItems(observableBooks);
        }
    }

    private List<GoogleBook> fetchGoogleBooks(String query) {
        List<GoogleBook> books = new ArrayList<>();
        try {
            String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query.replace(" ", "+");
            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setRequestMethod("GET");
            try (Reader reader = new InputStreamReader(conn.getInputStream());
                 Scanner scanner = new Scanner(reader)) {
                String response = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray items = jsonResponse.optJSONArray("items");
                if (items != null) {
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");
                        String title = volumeInfo.optString("title", "Unknown Title");

                        String author = "Unknown Author";
                        if (volumeInfo.has("authors")) {
                            JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                            if (authorsArray.length() > 0) {
                                author = authorsArray.getString(0);
                            }
                        }

                        String isbn = "Unknown ISBN";
                        if (volumeInfo.has("industryIdentifiers")) {
                            JSONArray identifiers = volumeInfo.getJSONArray("industryIdentifiers");
                            // Try to find ISBN_13 first.
                            for (int j = 0; j < identifiers.length(); j++) {
                                JSONObject idObj = identifiers.getJSONObject(j);
                                if ("ISBN_13".equals(idObj.optString("type"))) {
                                    isbn = idObj.optString("identifier", "Unknown ISBN");
                                    break;
                                }
                            }
                            // If not found, use the first identifier.
                            if ("Unknown ISBN".equals(isbn) && identifiers.length() > 0) {
                                isbn = identifiers.getJSONObject(0).optString("identifier", "Unknown ISBN");
                            }
                        }
                        books.add(new GoogleBook(title, author, isbn));
                    }
                }
            }
        } catch (Exception e) {
            books.add(new GoogleBook("Error fetching data", "", ""));
        }
        return books;
    }

    @FXML
    private TextField numberOfCopiesField;

    @FXML
    private void addGoogleBook() {
        try{
            GoogleBook selected = resultsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                int numberOfCopies = Integer.parseInt(numberOfCopiesField.getText().trim());
                Book newBook = new Book(0, selected.getTitle(), selected.getAuthor(), selected.getIsbn(), numberOfCopies);
                bookService.addBook(newBook);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Book added from Google successfully!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Please select a book from the list.");
            }
        } catch (NumberFormatException e)
        {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid number for the number of copies.");
        }
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

    private static class GoogleBook {
        private final String title;
        private final String author;
        private final String isbn;

        public GoogleBook(String title, String author, String isbn) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
        }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getIsbn() { return isbn; }
    }
}
