package com.maktabti.Controllers;

import com.maktabti.Entities.Book;
import com.maktabti.Services.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class BookController {
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, Integer> copiesColumn;
    @FXML private TextField searchField;

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
        List<Book> filtered = bookService.getAllBooks().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(query) ||
                        b.getAuthor().toLowerCase().contains(query) ||
                        b.getIsbn().toLowerCase().contains(query))
                .collect(Collectors.toList());
        bookList.clear();
        bookList.addAll(filtered);
        bookTable.setItems(bookList);
    }
}
