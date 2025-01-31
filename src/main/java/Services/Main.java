package Services;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {

    private static Connection con;
    private static final String URL = "jdbc:mysql://localhost:3306/Maktabti";
    private static final String USER = "root";
    private static final String PASS = "";

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LandingPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Maktabti - Home");
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToDatabase(); // Establish database connection after UI loads
    }

    private void connectToDatabase() {
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println(" Database connected successfully: " + con);
        } catch (SQLException e) {
            System.out.println(" Database connection failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args); // Start JavaFX application
    }
}
