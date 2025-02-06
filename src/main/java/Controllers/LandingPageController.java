package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class LandingPageController {

    @FXML
    private Button memberSignInBtn; // Injected from FXML

    @FXML
    private void handleAdminSignIn(ActionEvent event) {
        switchScene("/AccountSignUpIn.fxml"); // Navigate to admin login page
    }

    @FXML
    private void handleMemberSignUp(ActionEvent event) {
        switchScene("/AccountSignUpIn.fxml"); // Navigate to member sign-up page
    }

    @FXML
    private void handleMemberSignIn(ActionEvent event) {
        switchScene("/MemberLogin.fxml"); // Navigate to member login page
    }

    private void switchScene(String fxmlPath) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) memberSignInBtn.getScene().getWindow(); // Use any button's scene to get the stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML file: " + fxmlPath);
        }
    }
}