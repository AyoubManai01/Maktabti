package com.maktabti.Controllers;

import com.maktabti.Entities.User;
import com.maktabti.Services.UserService;
import com.maktabti.Utils.CurrentUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML private TextField signUpUsername;
    @FXML private PasswordField signUpPassword;
    @FXML private TextField signUpEmail;
    @FXML private Label signUpErrorLabel;

    @FXML private Button loginButton;
    @FXML private Button signUpButton;
    @FXML private Button backToLoginButton;

    private final UserService userService = new UserService();

    /**
     * Handles user login.
     */
    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password cannot be empty.", Alert.AlertType.ERROR);
            return;
        }

        // Authenticate user
        User user = userService.authenticate(username, password);
        if (user != null) {
            errorLabel.setText("Login successful!");
            // Navigate to the main application screen
            navigateToMainApp(user);
        } else {
            showAlert("Error", "Invalid credentials. Please try again.", Alert.AlertType.ERROR);
        }
        CurrentUser.setUserId(user.getId());
        CurrentUser.setEmail(user.getEmail());
    }

    /**
     * Handles user sign-up.
     */
    @FXML
    public void handleSignUp() {
        String username = signUpUsername.getText();
        String password = signUpPassword.getText();
        String email = signUpEmail.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Error", "All fields must be filled out.", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format. Please enter a valid email.", Alert.AlertType.ERROR);
            return;
        }

        // Create user
        boolean isUserCreated = userService.createUser(username, password, email);
        if (isUserCreated) {
            signUpErrorLabel.setText("Sign-up successful! You can now log in.");
        } else {
            showAlert("Error", "Error during sign-up. Please try again.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Navigates to the main application screen based on user role.
     */
    private void navigateToMainApp(User user) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root;

            // Load interface based on user role
            if ("admin".equals(user.getRole())) {
                root = FXMLLoader.load(getClass().getResource("/AdminMain.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("/ClientMain.fxml"));
            }

            Scene scene = new Scene(root, 1280, 768);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load main application.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Navigates to the Sign-Up page.
     */
    @FXML
    public void handleNavigateToSignUp() {
        try {
            Parent signUpView = FXMLLoader.load(getClass().getResource("/SignUp.fxml"));
            Scene signUpScene = new Scene(signUpView, 1024, 768);
            Stage window = (Stage) signUpButton.getScene().getWindow();
            window.setScene(signUpScene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load sign-up page.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Navigates back to the Login page.
     */
    @FXML
    public void handleBackToLogin() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Scene loginScene = new Scene(loginView, 1024, 768);
            Stage window = (Stage) backToLoginButton.getScene().getWindow();
            window.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load login page.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleBackToLogin(ActionEvent event) throws IOException {
        Parent loginView = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        Scene loginScene = new Scene(loginView, 1024, 768);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    /**
     * Shows an alert message.
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Validates the email format using a regular expression.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
