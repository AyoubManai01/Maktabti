package com.maktabti;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the login screen
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));

        // Create a scene with the loaded FXML root and set dimensions
        Scene scene = new Scene(root, 1024, 768);

        // Add the global stylesheet to the scene
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Set the title of the application window
        primaryStage.setTitle("Maktabti Library Management System");

        // Set the scene to the stage (window)
        primaryStage.setScene(scene);

        // Show the window
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}
