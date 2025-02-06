package com.maktabti.Controllers;

import com.maktabti.Entities.User;
import com.maktabti.Services.UserService;
import com.maktabti.Utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UserService userService = new UserService();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userService.authenticate(username, password);
        if (user != null) {
            // Save the logged-in user in the session.
            Session.setCurrentUser(user);

            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Parent root;
                // Load different interfaces based on the user role.
                if ("admin".equals(user.getRole())) {
                    root = FXMLLoader.load(getClass().getResource("/AdminMain.fxml"));
                } else {
                    root = FXMLLoader.load(getClass().getResource("/ClientMain.fxml"));
                }
                Scene scene = new Scene(root, 1024, 768);
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                stage.setScene(scene);
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("Failed to load main application");
            }
        } else {
            errorLabel.setText("Invalid credentials");
        }
    }
}
