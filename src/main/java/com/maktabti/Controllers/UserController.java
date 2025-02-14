package com.maktabti.Controllers;

import com.maktabti.Entities.User;
import com.maktabti.Services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TextField searchField;
    @FXML private Button addUserButton;
    @FXML private Button editUserButton;
    @FXML private Button deleteUserButton;
    @FXML private Button showStatsButton;  // New button for statistics

    private UserService userService = new UserService();
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private User selectedUser = null;

    @FXML
    public void initialize() {
        // Bind columns to User properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Populate role combo box
        roleComboBox.setItems(FXCollections.observableArrayList("client", "admin"));

        // Load all users into the table
        refreshTable();

        // Enable/disable edit and delete buttons based on selection
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedUser = newSelection;
                editUserButton.setDisable(false);
                deleteUserButton.setDisable(false);
                populateForm(selectedUser);
            } else {
                selectedUser = null;
                editUserButton.setDisable(true);
                deleteUserButton.setDisable(true);
                clearForm();
            }
        });
    }

    @FXML
    private void addUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert("Error", "All fields are required!");
            return;
        }

        User user = new User(0, username, password, role);
        userService.addUser(user);
        refreshTable();
        clearForm();
    }

    @FXML
    private void editUser() {
        if (selectedUser == null) {
            showAlert("Error", "No user selected!");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert("Error", "All fields are required!");
            return;
        }

        selectedUser.setUsername(username);
        selectedUser.setPassword(password);
        selectedUser.setRole(role);

        userService.updateUser(selectedUser);
        refreshTable();
        clearForm();
    }

    @FXML
    private void deleteUser() {
        if (selectedUser == null) {
            showAlert("Error", "No user selected!");
            return;
        }

        boolean confirmed = showConfirmation("Delete User", "Are you sure you want to delete this user?");
        if (confirmed) {
            userService.deleteUser(selectedUser.getId());
            refreshTable();
            clearForm();
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshTable();
        } else {
            userList.clear();
            userList.addAll(userService.searchUsers(query));
            userTable.setItems(userList);
        }
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        refreshTable();
    }

    @FXML
    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        selectedUser = null;
        editUserButton.setDisable(true);
        deleteUserButton.setDisable(true);
    }

    private void populateForm(User user) {
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        roleComboBox.setValue(user.getRole());
    }

    private void refreshTable() {
        userList.clear();
        userList.addAll(userService.getAllUsers());
        userTable.setItems(userList);
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(null) == ButtonType.OK;
    }

    // ---------- New Method: Show User Statistics using data from the database ----------
    @FXML
    private void showStatistics() {
        try {
            // Call the new service method to get statistics from the users table in maktabti_db
            JSONObject stats = userService.getUserStatistics();
            int totalUsers = stats.optInt("totalUsers", 0);
            int adminCount = stats.optInt("adminCount", 0);
            int clientCount = stats.optInt("clientCount", 0);
            String statsMessage = "User Statistics:\n\nTotal Users: " + totalUsers +
                    "\nAdmins: " + adminCount +
                    "\nClients: " + clientCount;
            Alert statsAlert = new Alert(Alert.AlertType.INFORMATION);
            statsAlert.setTitle("User Statistics");
            statsAlert.setHeaderText("Statistics from Database");
            statsAlert.setContentText(statsMessage);
            statsAlert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to fetch user statistics from the database.");
        }
    }


}
