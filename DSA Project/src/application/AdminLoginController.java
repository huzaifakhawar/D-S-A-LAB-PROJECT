package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminLoginController {

    @FXML
    private Button backButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button adminLoginButton;

    // HashMap to simulate a database for storing admin credentials (DSA: HashMap)
    private Map<String, String> adminDatabase;

    // Constructor to initialize the admin credentials database (only using HashMap for credentials)
    public AdminLoginController() {
        adminDatabase = new HashMap<>();  // Using HashMap for quick lookup of admin credentials
        
        // Example admin credentials (email and password)
        adminDatabase.put("admin123@gmail.com", "admin123");
        adminDatabase.put("admin456@gmail.com", "admin123");
    }

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
    private void handleBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        applyFadeTransition(currentStage.getScene().getRoot(), 1.0, 0.0, 0.5, e -> {
            currentStage.setScene(scene);
            applyFadeTransition(root, 0.0, 1.0, 0.5, null);
        });
    }

    private void applyFadeTransition(javafx.scene.Node node, double fromValue, double toValue, double duration, EventHandler<ActionEvent> onFinished) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), node);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setOnFinished(onFinished);
        fadeTransition.play();
    }

    @FXML
    private void handleBackMouseExit(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.DEFAULT);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
        backButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    @FXML
    private void handleButtonMouseExit(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.DEFAULT);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();

        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #424242; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    @FXML
    private void handleButtonMouseEnter(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.HAND);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.15);
        scaleTransition.setToY(1.15);
        scaleTransition.play();

        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #38e4c5; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    // Method to handle login button action
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (validateLogin(email, password)) {
            loadAdminDashboard();
        } else {
            showAlert("Error", "Invalid email or password.");
        }
    }

    // Method to validate login credentials using HashMap
    private boolean validateLogin(String email, String password) {
        // Check if email exists and password matches in the HashMap (DSA: HashMap lookup)
        return adminDatabase.containsKey(email) && adminDatabase.get(email).equals(password);
    }

    // Method to load Admin dashboard
    private void loadAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load Admin dashboard.");
        }
    }

    // Method to show an alert dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
