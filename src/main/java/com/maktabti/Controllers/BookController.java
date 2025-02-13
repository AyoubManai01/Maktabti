package com.maktabti.Controllers;

import com.maktabti.Entities.Book;
import com.maktabti.Services.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
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
    // ----- Table (Normal) View Fields -----
    @FXML private AnchorPane normalView; // Container for the table view
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

    // ----- Google Books Search Fields -----
    @FXML private TextField searchField1;
    @FXML private Button searchButton;
    @FXML private ListView<GoogleBook> resultsList;
    @FXML private Button addGoogleBookButton;
    @FXML private TextField numberOfCopiesField;

    // ----- Catalog View Fields -----
    @FXML private AnchorPane catalogView; // Container for the catalog view
    @FXML private FlowPane catalogPane;   // Grid to display book cards

    private final BookService bookService = new BookService();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    // Flag: false = normal (table) view, true = catalog view
    private boolean isCatalogView = true;

    @FXML
    public void initialize() {
        // Set default: normal view visible, catalog view hidden.
        if (normalView != null) {
            normalView.setVisible(true);
        }
        if (catalogView != null) {
            catalogView.setVisible(false);
        }

        // Initialize the table view if present
        if (idColumn != null && titleColumn != null && authorColumn != null && isbnColumn != null && copiesColumn != null) {
            idColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            titleColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
            authorColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAuthor()));
            isbnColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIsbn()));
            copiesColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAvailableCopies()).asObject());
            refreshTable();
        }

        // Initialize Google Books search UI if present
        if (searchField1 != null && searchButton != null && resultsList != null) {
            searchButton.setOnAction(e -> searchGoogleBooks());
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
        if (catalogView != null) catalogView.setVisible(true);
        if (normalView != null) normalView.setVisible(false);
        refreshCatalog();
        if (logoImageView != null) {
            logoImageView.setImage(new Image(getClass().getResourceAsStream("/google-bg.jpg")));
        }
    }
    @FXML
    private ImageView logoImageView;

    private void refreshTable() {
        bookList.setAll(bookService.getAllBooks());
        if (bookTable != null) {
            bookTable.setItems(bookList);
        }
    }

    @FXML
    public void searchBooks() {
        String query = searchField.getText().trim().toLowerCase();
        bookList.setAll(bookService.getAllBooks().stream().filter(b ->
                b.getTitle().toLowerCase().contains(query) ||
                        b.getAuthor().toLowerCase().contains(query) ||
                        b.getIsbn().toLowerCase().contains(query)
        ).toList());
        if (!isCatalogView && bookTable != null) {
            bookTable.setItems(bookList);
        } else if (isCatalogView) {
            refreshCatalog();
        }
    }

    @FXML
    public void removeBook() {
        // For table view removal (additional logic can be added for catalog view selection)
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected != null && bookService.removeBook(selected.getId())) {
            refreshTable();
            if (isCatalogView) {
                refreshCatalog();
            }
            showAlert(Alert.AlertType.INFORMATION, "Book removed successfully!");
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
            // When manually adding, we set coverUrl to an empty string.
            bookService.addBook(new Book(0, title, author, isbn, availableCopies, ""));
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

    // ---------- Google Books Search Methods ----------
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
                            for (int j = 0; j < identifiers.length(); j++) {
                                JSONObject idObj = identifiers.getJSONObject(j);
                                if ("ISBN_13".equals(idObj.optString("type"))) {
                                    isbn = idObj.optString("identifier", "Unknown ISBN");
                                    break;
                                }
                            }
                            if ("Unknown ISBN".equals(isbn) && identifiers.length() > 0) {
                                isbn = identifiers.getJSONObject(0).optString("identifier", "Unknown ISBN");
                            }
                        }
                        // Extract cover image URL if available
                        String coverUrl = "";
                        if (volumeInfo.has("imageLinks")) {
                            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                            coverUrl = imageLinks.optString("thumbnail", "");
                        }
                        books.add(new GoogleBook(title, author, isbn, coverUrl));
                    }
                }
            }
        } catch (Exception e) {
            books.add(new GoogleBook("Error fetching data", "", "", ""));
        }
        return books;
    }

    @FXML
    private void addGoogleBook() {
        try {
            GoogleBook selected = resultsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                int numberOfCopies = Integer.parseInt(numberOfCopiesField.getText().trim());
                // Create a new Book using the details from GoogleBook (include coverUrl)
                Book newBook = new Book(0, selected.getTitle(), selected.getAuthor(), selected.getIsbn(), numberOfCopies, selected.getCoverUrl());
                bookService.addBook(newBook);
                refreshTable();
                showAlert(Alert.AlertType.INFORMATION, "Book added from Google successfully!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Please select a book from the list.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid number for the number of copies.");
        }
    }

    @FXML
    private void openGoogleBooksSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GoogleBooksSearch.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) searchField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminMain.fxml"));
            Parent bookPane = loader.load();
            Stage stage = (Stage) (searchField1 != null ? searchField1.getScene().getWindow() : catalogPane.getScene().getWindow());
            stage.getScene().setRoot(bookPane);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- Toggle View: Switch between normal (table) and catalog view ----------
    @FXML
    public void switchBooksView() {
        if (isCatalogView) {
            // Switch to normal view
            if (normalView != null) normalView.setVisible(true);
            if (catalogView != null) catalogView.setVisible(false);
            refreshTable();
        } else {
            // Switch to catalog view
            if (normalView != null) normalView.setVisible(false);
            if (catalogView != null) {
                catalogView.setVisible(true);
                refreshCatalog();
            }
        }
        isCatalogView = !isCatalogView; // Toggle state
    }

    // ---------- Catalog Grid Builder ----------
    public void refreshCatalog() {
        if (catalogPane == null) return;
        catalogPane.getChildren().clear();
        List<Book> books = bookService.getAllBooks();
        for (Book book : books) {
            // Create a card for each book
            VBox card = new VBox(10);
            card.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-padding: 10; " +
                    "-fx-background-radius: 8; -fx-border-radius: 8;");
            card.setPrefWidth(150);

            // Create an ImageView for the cover image
            ImageView coverImageView = new ImageView();
            coverImageView.setFitWidth(120);
            coverImageView.setPreserveRatio(true);
            String coverUrl = book.getCoverUrl();
            if (coverUrl != null && !coverUrl.isEmpty()) {
                coverImageView.setImage(new Image(coverUrl, true));
            } else {
                // Use a placeholder image (ensure placeholder.png exists in your resources)
                coverImageView.setImage(new Image(getClass().getResourceAsStream("/placeholder.png")));
            }

            // Labels for title and author
            Label titleLabel = new Label(book.getTitle());
            titleLabel.setStyle("-fx-font-weight: bold; -fx-wrap-text: true;");
            titleLabel.setMaxWidth(120);
            Label authorLabel = new Label(book.getAuthor());
            authorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

            card.getChildren().addAll(coverImageView, titleLabel, authorLabel);
            // Optionally, add a click handler to show more details
            card.setOnMouseClicked(e -> {
                // TODO: Show book details in a new view or popup if desired
            });
            catalogPane.getChildren().add(card);
        }
    }

    // ---------- Inner Class for Google Books results ----------
    private static class GoogleBook {
        private final String title;
        private final String author;
        private final String isbn;
        private final String coverUrl;

        public GoogleBook(String title, String author, String isbn, String coverUrl) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.coverUrl = coverUrl;
        }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getIsbn() { return isbn; }
        public String getCoverUrl() { return coverUrl; }
    }
}
