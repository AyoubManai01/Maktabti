package com.maktabti.Controllers;

import com.maktabti.Entities.User;
import com.maktabti.Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Parent root;
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
