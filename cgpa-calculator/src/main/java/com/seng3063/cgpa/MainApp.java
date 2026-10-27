package com.seng3063.cgpa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main entry point for the CGPA Calculator JavaFX application.
 * 
 * SENG3063 OOP and GUI Review — Lab Exercise 01
 * 
 * Launches the JavaFX runtime, loads the FXML layout and CSS stylesheet,
 * and displays the main application window.
 * 
 * The application entry point is kept separate from the controller
 * to follow clean architecture and separation of concerns.
 */
public class MainApp extends Application {

    /** Default window width */
    private static final double DEFAULT_WIDTH = 1100;
    /** Default window height */
    private static final double DEFAULT_HEIGHT = 750;
    /** Minimum window width */
    private static final double MIN_WIDTH = 800;
    /** Minimum window height */
    private static final double MIN_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML layout
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("main-view.fxml"));
        Parent root = loader.load();

        // Create the scene
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // Configure the primary stage
        primaryStage.setTitle("CGPA Calculator — Student Academic Performance");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        // Show the window
        primaryStage.show();
    }

    /**
     * Application entry point.
     * Calls Application.launch() to start the JavaFX runtime.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
