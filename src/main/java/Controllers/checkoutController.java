package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class checkoutController {

    @FXML private TableView<BookItem> booksTable;
    @FXML private TableColumn<BookItem, String> bookTitleColumn;
    @FXML private TableColumn<BookItem, Double> priceColumn;
    @FXML private TableColumn<BookItem, Integer> quantityColumn;
    @FXML private Label totalAmountLabel;
    @FXML private Button cashButton, creditCardButton, checkButton;

    private ObservableList<BookItem> bookItems = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up table columns
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Sample data
        bookItems.add(new BookItem("Java Programming", 29.99, 1));
        bookItems.add(new BookItem("Design Patterns", 39.99, 2));

        booksTable.setItems(bookItems);

        updateTotal();

        // Payment buttons
        cashButton.setOnAction(event -> processPayment("Cash"));
        creditCardButton.setOnAction(event -> processPayment("Credit Card"));
        checkButton.setOnAction(event -> processPayment("Check"));
    }

    private void updateTotal() {
        double total = bookItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        totalAmountLabel.setText("$" + String.format("%.2f", total));
    }

    private void processPayment(String method) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Success");
        alert.setHeaderText(null);
        alert.setContentText("You have successfully paid with " + method + ".");
        alert.showAndWait();
    }

    // Inner class to represent book items
    public static class BookItem {
        private String title;
        private double price;
        private int quantity;

        public BookItem(String title, double price, int quantity) {
            this.title = title;
            this.price = price;
            this.quantity = quantity;
        }

        public String getTitle() { return title; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
    }
}
