package Controllers;

import Entites.Account;
import Services.CreateAccountService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class AccountSignUpIn {

    private final CreateAccountService accountService = new CreateAccountService(); // Service for authentication

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInBtn;
    @FXML
    private Button signUpBtn;
    @FXML
    private Text errorMessage; // For displaying login errors

    @FXML
    private void handleSignIn(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (accountService.isValidUser(username, password)) {
            switchScene("AdminDashboard.fxml"); // Load the Admin Dashboard if login is successful
        } else {
            errorMessage.setText("Invalid credentials. Try again!");
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (accountService.createMemberAccount(username, password)) {
            switchScene("MemberDashboard.fxml"); // Navigate to Member Dashboard after successful signup
        } else {
            errorMessage.setText("Signup failed! Username may already exist.");
        }
    }

    private void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) signInBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}