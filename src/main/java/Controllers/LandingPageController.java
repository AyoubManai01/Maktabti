package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LandingPageController {

    @FXML
    private void handleAdminSignIn() {
        switchScene(); // Navigate to login page
    }

    @FXML
    private void handleMemberSignUp() {
        switchScene(); // Navigate to sign-up page
    }

    private void switchScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + "AccountSignUpIn.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) root.getScene().getWindow(); // Get current stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
