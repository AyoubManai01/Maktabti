package com.maktabti.Controllers;

import com.maktabti.Entities.User;
import com.maktabti.Services.UserService;
import com.maktabti.Utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    public Button signupButton;
    @FXML private TextField usernameField;
    @FXML private TextField signUpEmail;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML private Button loginButton;
    @FXML private TextField signUpUsername;
    @FXML private PasswordField signUpPassword;
    
    @FXML private Label signUpErrorLabel;
    @FXML private Button signUpButton; // Ensure it's linked in FXML
    @FXML private Button backToLoginButton; // Ensure it's linked in FXML

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
            return;
        }

        User user = userService.authenticate(username, password);
        if (user != null) {
            Session.setCurrentUser(user); // Save the user in session

            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Parent root;

                // Load interface based on user role
                if ("admin".equals(user.getRole())) {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AdminMain.fxml")));
                } else {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ClientMain.fxml")));
                }

                Scene scene = new Scene(root, 1024, 768);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Failed to load main application.");
            }
        } else {
            errorLabel.setText("Invalid credentials. Please try again.");
        }
    }

    @FXML
    public void handleSignUp() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = signUpEmail.getText(); // Assuming you have an email field

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            if (errorLabel != null) {
                errorLabel.setText("All fields must be filled out.");
            } else {
                System.out.println("Error: errorLabel is null.");
            }
            return;
        }

        // Assuming you have a service to handle user registration
        boolean isUserCreated = userService.createUser(username, password, email);

        if (isUserCreated) {
            if (errorLabel != null) {
                errorLabel.setText("Sign-up successful! You can now log in.");
            } else {
                System.out.println("Error: errorLabel is null.");
            }
        } else {
            if (errorLabel != null) {
                errorLabel.setText("Error during sign-up. Please try again.");
            } else {
                System.out.println("Error: errorLabel is null.");
            }
        }
    }




}
