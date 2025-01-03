package application;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton, registerButton, deleteButton;  // Add buttons to be handled

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    // Stack and Queue for action history and user requests
    private Stack<String> actionHistory;  // Stack for login history
    private Queue<String> userQueue;      // Queue for user requests

    public LoginController() {
        actionHistory = new Stack<>();
        userQueue = new LinkedList<>();
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        String username = validateLogin(email, password);
        
        // Log the action in Stack (Action History)
        actionHistory.push("Login attempted by: " + email);
        
        if (username != null) {
            showAlert("Login Successful", "Welcome " + username);
            loadDashboard(); // Call the method to load the dashboard
        } else {
            showAlert("Login Failed", "Invalid email or password");
        }
    }

    @FXML
    void handleRegister(ActionEvent event) {
        try {
            // Log the action in Stack (Action History)
            actionHistory.push("Register button clicked");
            
            // Load the Registration screen FXML
            Parent registrationRoot = FXMLLoader.load(getClass().getResource("Registration.fxml"));
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            Scene registrationScene = new Scene(registrationRoot);

            // Apply fade-out effect to the current scene
            applyFadeTransition(currentStage.getScene().getRoot(), 1.0, 0.0, 0.5, e -> { 
                // After fade-out, switch the scene
                currentStage.setScene(registrationScene);
                // Apply fade-in effect to the new scene
                applyFadeTransition(registrationRoot, 0.0, 1.0, 0.5, null);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        if (!email.isEmpty() && !password.isEmpty()) {
            String username = getUsernameByEmail(email);
            if (username != null && deleteUser(email, password)) {
                // Log the action in Queue (User Requests)
                userQueue.offer("User " + username + " deleted");
                showAlert("Deletion Successful", username + " has been deleted from the Data.");
            } else {
                showAlert("Deletion Failed", "Could not delete user. User might not exist.");
            }
        } else {
            showAlert("Deletion Failed", "Please enter both email and password.");
        }
    }

    // Method for applying fade transition
    private void applyFadeTransition(javafx.scene.Node node, double fromValue, double toValue, double duration, EventHandler<ActionEvent> onFinished) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), node);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setOnFinished(onFinished);
        fadeTransition.play();
    }

    // Mouse enter effect for buttons
    @FXML
    private void handleButtonMouseEnter(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.HAND);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();

        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #38e4c5; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    public void handleDeleteButtonMouseEnter(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.HAND);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
        deleteButton.setStyle("-fx-background-color: #FF1D18; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    public void handleDeleteButtonMouseExit(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.DEFAULT);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
        deleteButton.setStyle("-fx-background-color: #424242; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
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

    private String validateLogin(String email, String password) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT username FROM users WHERE email = ? AND password = ?")) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getUsernameByEmail(String email) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT username FROM users WHERE email = ?")) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean deleteUser(String email, String password) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE email = ? AND password = ?")) {
            statement.setString(1, email);
            statement.setString(2, password);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadDashboard() {
        try {
            // Load the "dashboard.fxml" file
            Parent dashboardRoot = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
            Stage currentStage = (Stage) emailField.getScene().getWindow();

            Scene scene = new Scene(dashboardRoot);

            // Apply fade-out effect to the current scene
            applyFadeTransition(currentStage.getScene().getRoot(), 1.0, 0.0, 0.5, e -> {
                // After fade-out is complete, switch the scene
                currentStage.setScene(scene);

                // Apply fade-in effect to the new scene
                applyFadeTransition(dashboardRoot, 0.0, 1.0, 0.5, null);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
