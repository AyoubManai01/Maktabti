package com.maktabti.Controllers;

import com.maktabti.Entities.User;
import com.maktabti.Services.UserService;
import com.maktabti.Controllers.EmailService;
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

    @FXML private Button signupButton;
    @FXML private ComboBox roleComboBox;
    @FXML private TextField usernameField;
    @FXML private TextField signUpEmail;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML private Label signUpErrorLabel;
    @FXML private Button loginButton;

    @FXML private TextField signUpUsername;
    @FXML private PasswordField signUpPassword;

    @FXML private Button signUpButton; // Ensure it's linked in FXML
    @FXML private Button backToLoginButton; // Ensure it's linked in FXML

    private final UserService userService = new UserService();
    private final EmailService emailService = new EmailService();  // Initialize the email service

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

            // Send a login success email using the existing emailService
            String subject = "Login Successful";
            String body = "Dear " + user.getUsername() + ",\n\nYou have successfully logged in to your account.";
            emailService.sendEmail(user.getEmail(), subject, body);  // Send email to the user

            // Check the console for feedback about email sending
            System.out.println("Login email sent to: " + user.getEmail());

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
        String username = signUpUsername.getText();  // Use signUpUsername for sign-up
        String password = signUpPassword.getText();  // Use signUpPassword for sign-up
        String email = signUpEmail.getText(); // Use signUpEmail for sign-up

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            signUpErrorLabel.setText("All fields must be filled out.");
            return;
        }

        // Assuming you have a service to handle user registration
        boolean isUserCreated = userService.createUser(username, password, email);

        if (isUserCreated) {
            // Send sign-up success email
            String emailSubject = "Account Created Successfully";
            String emailBody = "Dear " + username + ",\n\nYour account has been successfully created. You can now log in to your account.";
            // Inside your handleSignUp method
            emailService.sendEmail(email, "Account Created Successfully", "Dear " + username + ",\n\nYour account has been successfully created. You can now log in to your account.");


            // Check the console for feedback about email sending
            System.out.println("Sign-up email sent to: " + email);

            signUpErrorLabel.setText("Sign-up successful! You can now log in.");
        } else {
            signUpErrorLabel.setText("Error during sign-up. Please try again.");
        }
    }

    /**
     * Handles the back-to-login action.
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
