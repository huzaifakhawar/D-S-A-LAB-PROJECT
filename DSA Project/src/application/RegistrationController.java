package application;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class RegistrationController {

    // Stack to simulate undo functionality for previous registrations
    private static Stack<String> registrationStack = new Stack<>();

    // Queue to simulate the order of registration attempts (First-Come-First-Served)
    private static Queue<String> registrationQueue = new LinkedList<>();

    // LinkedList to store users temporarily (just to show LinkedList usage)
    private static LinkedList<String> usersList = new LinkedList<>();

    public void initialize() {
        backButton.setText("\u2190");
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField usernameField;

    @FXML
    private Button backButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Invalid Input", "All fields are required.");
            return;
        }

        if (!email.toLowerCase().endsWith("@gmail.com")) {
            showAlert("Invalid Email", "Please enter a valid Gmail address.");
            return;
        }

        // Using Stack to "undo" registration (for demo purposes)
        registrationStack.push(username); // Save the last registration for potential "undo"

        // Using Queue to simulate registration order (FIFO)
        registrationQueue.offer(username); // Add to the queue to process the registration in order

        // Using LinkedList to store users temporarily
        usersList.add(username);  // Add the user to the list temporarily

        if (registerUser(username, password, email)) {
            showAlert("Registration Successful", "User " + username + " has been registered.");
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        } else {
            showAlert("Registration Failed", "An error occurred while registering. Please try again.");
        }
    }

    public void handleBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleBackMouseEnter(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.HAND);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
        backButton.setStyle("-fx-background-color: #38e4c5; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    public void handleBackMouseExit(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.DEFAULT);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
        backButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean registerUser(String username, String password, String email) {
        String dbUrl = "jdbc:mysql://localhost:3306/hospital";
        String dbUser = "root";
        String dbPassword = "S@fyan82";

        String queryInsert = "INSERT INTO users (Username, Email, Password) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = conn.prepareStatement(queryInsert)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
