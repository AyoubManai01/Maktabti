package com.maktabti.Controllers;

import com.maktabti.Entities.User;
import com.maktabti.Services.UserService;
import com.maktabti.Utils.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

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
            errorLabel.setText("Username and password cannot be empty.");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username and password cannot be empty.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Authenticate user
        User user = userService.authenticate(username, password);
        if (user != null) {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Login successful!", ButtonType.OK);
            successAlert.showAndWait();
            // Set current user info
            CurrentUser.setUserId(user.getId());
            CurrentUser.setEmail(user.getEmail());
            // Navigate to the main application screen based on role
            navigateToMainApp(user);
        } else {
            errorLabel.setText("Invalid credentials. Please try again.");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid credentials. Please try again.", ButtonType.OK);
            errorAlert.showAndWait();
        }
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
            signUpErrorLabel.setText("All fields must be filled out.");
            Alert alert = new Alert(Alert.AlertType.ERROR, "All fields must be filled out.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Validate email format using a simple regex
        if (!isValidEmail(email)) {
            signUpErrorLabel.setText("Invalid email format.");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid email address.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Create user
        boolean isUserCreated = userService.createUser(username, password, email);
        if (isUserCreated) {
            signUpErrorLabel.setText("Sign-up successful! You can now log in.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sign-up successful! You can now log in.", ButtonType.OK);
            alert.showAndWait();
        } else {
            signUpErrorLabel.setText("Error during sign-up. Please try again.");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error during sign-up. Please try again.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Validates the email format using a regular expression.
     * @param email The email to validate.
     * @return true if the email is valid; false otherwise.
     */
    private boolean isValidEmail(String email) {
        // Basic regex for email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    /**
     * Navigates to the main application screen based on user role.
     */
    private void navigateToMainApp(User user) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root;
            // Load interface based on user role
            if ("admin".equalsIgnoreCase(user.getRole())) {
                root = FXMLLoader.load(getClass().getResource("/AdminMain.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("/ClientMain.fxml"));
            }
            Scene scene = new Scene(root, 1280, 768);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load main application.");
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
        }
    }

    /**
     * Navigates back to the Login page.
     */
    @FXML
    public void handleBackToLogin(ActionEvent event) throws IOException {
        Parent loginView = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        Scene loginScene = new Scene(loginView, 1024, 768);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }
}
