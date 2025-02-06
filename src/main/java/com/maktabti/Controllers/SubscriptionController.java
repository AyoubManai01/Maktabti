package com.maktabti.Controllers;

import com.maktabti.Entities.Subscription;
import com.maktabti.Services.SubscriptionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class SubscriptionController {
    @FXML
    private TableView<Subscription> subscriptionTable;
    @FXML
    private TableColumn<Subscription, Integer> idColumn;
    @FXML
    private TableColumn<Subscription, Integer> userIdColumn;
    @FXML
    private TableColumn<Subscription, String> startDateColumn;
    @FXML
    private TableColumn<Subscription, String> endDateColumn;
    @FXML
    private TableColumn<Subscription, Double> fineColumn;
    @FXML
    private TextField userIdField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField fineField;
    @FXML
    private Button addSubscriptionButton;

    private SubscriptionService subscriptionService = new SubscriptionService();
    private ObservableList<Subscription> subscriptionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        userIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getUserId()).asObject());
        startDateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStartDate().toString()));
        endDateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEndDate().toString()));
        fineColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getFine()).asObject());
        refreshTable();
    }

    @FXML
    private void addSubscription() {
        int userId = Integer.parseInt(userIdField.getText());
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        double fine = Double.parseDouble(fineField.getText());
        Subscription subscription = new Subscription(0, userId, startDate, endDate, fine);
        subscriptionService.addSubscription(subscription);
        refreshTable();
    }

    private void refreshTable() {
        subscriptionList.clear();
        subscriptionList.addAll(subscriptionService.getAllSubscriptions());
        subscriptionTable.setItems(subscriptionList);
    }
}
