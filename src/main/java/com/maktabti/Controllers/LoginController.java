package com.maktabti.Controllers;

import com.maktabti.Entities.User;
import com.maktabti.Services.UserService;
import com.maktabti.Utils.CurrentUser;
import com.maktabti.Controllers.EmailService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    @FXML private TextField signUpUsername;
    @FXML private PasswordField signUpPassword;
    @FXML private TextField signUpEmail;
    @FXML private Label signUpErrorLabel;
    @FXML private Button signUpButton;

    private final UserService userService = new UserService();
    private final EmailService emailService = new EmailService();

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
            // ✅ Store user info in CurrentUser class
            CurrentUser.setUserId(user.getId());
            CurrentUser.setEmail(user.getEmail());

            // ✅ Debugging output to ensure user data is stored
            System.out.println("✅ User Logged In:");
            System.out.println("   - ID: " + CurrentUser.getUserId());
            System.out.println("   - Email: " + CurrentUser.getEmail());

            // ✅ Send login email
            emailService.sendEmail(user.getEmail(), "Login Successful",
                    "Dear " + user.getUsername() + ",\n\nYou have successfully logged in to your account.");

            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Parent root;

                // ✅ Load correct interface based on role
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
            // ✅ Send welcome email
            emailService.sendEmail(email, "Account Created Successfully",
                    "Dear " + username + ",\n\nYour account has been successfully created. You can now log in.");

            signUpErrorLabel.setText("Sign-up successful! You can now log in.");
        } else {
            signUpErrorLabel.setText("Error during sign-up. Please try again.");
        }
    }
}
