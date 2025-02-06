package com.maktabti.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class OrderBookController {
    @FXML
    private TextField bookIdField;
    @FXML
    private Label orderStatus;

    @FXML
    public void orderBook() {
        orderStatus.setText("Order placed for Book ID: " + bookIdField.getText());
    }
}
