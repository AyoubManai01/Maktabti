package com.maktabti.Controllers;

import com.maktabti.Entities.User;
import com.maktabti.Services.UserService;
import com.maktabti.Utils.CurrentUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    @FXML private TextField signUpUsername;
    @FXML private TextField signUpEmail;
    @FXML private PasswordField signUpPassword;
    @FXML private Label signUpErrorLabel;
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
            return;
        }

        User user = userService.authenticate(username, password);
        if (user != null) {
            // ✅ Store the logged-in user in `CurrentUser`
            CurrentUser.setUserId(user.getId());
            CurrentUser.setEmail(user.getEmail());

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
            return;
        }

        boolean isUserCreated = userService.createUser(username, password, email);

        if (isUserCreated) {
            signUpErrorLabel.setText("Sign-up successful! You can now log in.");
        } else {
            signUpErrorLabel.setText("Error during sign-up. Please try again.");
        }
    }

    /**
     * Handles back to login action.
     */
    @FXML
    public void handleBackToLogin(ActionEvent event) throws IOException {
        Parent loginView = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        Scene loginScene = new Scene(loginView);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }
}
