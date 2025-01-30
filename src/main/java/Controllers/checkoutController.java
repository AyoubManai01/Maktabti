package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class checkoutController {

    @FXML private TextField bookTitleField;
    @FXML private TextField authorField;
    @FXML private TextField isbnField;
    @FXML private TextField memberIdField;
    @FXML private TextField memberNameField;
    @FXML private DatePicker dueDatePicker;
    @FXML private Button checkoutButton;
    @FXML private Button cancelButton;

    @FXML
    private void initialize() {
        checkoutButton.setOnAction(event -> processCheckout());
        cancelButton.setOnAction(event -> clearFields());
    }

    private void processCheckout() {
        String memberId = memberIdField.getText();
        String memberName = memberNameField.getText();
        String dueDate = dueDatePicker.getValue() != null ? dueDatePicker.getValue().toString() : "";

        if (memberId.isEmpty() || memberName.isEmpty() || dueDate.isEmpty()) {
            showAlert("Error", "Please fill all fields before checking out.");
            return;
        }

        System.out.println("Checkout successful for Member: " + memberName);
        showAlert("Success", "Book checked out successfully!");
        clearFields();
    }

    private void clearFields() {
        memberIdField.clear();
        memberNameField.clear();
        dueDatePicker.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
