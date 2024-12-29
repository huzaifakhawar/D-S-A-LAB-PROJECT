package application;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

public class NurseLoginController {

    @FXML
    private Button backButton;

    @FXML
    private Button nurseLoginButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    // Stack and Queue to store login attempts
    private Stack<String> loginHistory = new Stack<>();
    private Queue<String> loginQueue = new LinkedList<>();

    @FXML
    private void handleBackMouseEnter(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.HAND);

        // Apply scale transition (zoom in)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
        backButton.setStyle("-fx-background-color: #38e4c5; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    @FXML
    private void handleBackMouseExit(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.DEFAULT);

        // Reset scale back to normal size (zoom out)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
        backButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    @FXML
    private void handleButtonMouseExit(javafx.scene.input.MouseEvent event) {
        // Reset cursor when mouse exits the button
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.DEFAULT);

        // Reset scale back to normal size (zoom out)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();

        // Revert button background color when mouse exits
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #424242; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    @FXML
    private void handleButtonMouseEnter(javafx.scene.input.MouseEvent event) {
        // Change cursor to hand when mouse enters the button
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.HAND);

        // Apply scale transition (zoom in)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.15);
        scaleTransition.setToY(1.15);
        scaleTransition.play();

        // Change button background color when mouse enters
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #38e4c5; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        // Load the "login.fxml" file
        Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        // Apply fade-out effect to the current scene
        applyFadeTransition(currentStage.getScene().getRoot(), 1.0, 0.0, 0.5, e -> {
            // After fade-out is complete, switch the scene
            currentStage.setScene(scene);

            // Apply fade-in effect to the new scene
            applyFadeTransition(root, 0.0, 1.0, 0.5, null);
        });
    }

    // Method for applying fade transition
    private void applyFadeTransition(javafx.scene.Node node, double fromValue, double toValue, double duration, EventHandler<ActionEvent> onFinished) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), node);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setOnFinished(onFinished);
        fadeTransition.play();
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Log login attempt to Stack and Queue
        loginHistory.push("Login attempt by: " + email); // Stack stores last attempt first
        loginQueue.offer("Login attempt by: " + email); // Queue stores the first attempt first

        // Display login attempts in Stack and Queue for debugging
        System.out.println("Login History Stack: " + loginHistory);
        System.out.println("Login Queue: " + loginQueue);

        // Check if email and password match
        if (email.equals("nurse123@gmail.com") && password.equals("nurse123")) {
            // Load Nurse dashboard
            loadNurseDashboard();
        } else {
            showAlert("Error", "Invalid email or password.");
        }
    }

    private void loadNurseDashboard() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("nurse.fxml"));
            javafx.scene.Parent root = loader.load();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to load Nurse dashboard.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
