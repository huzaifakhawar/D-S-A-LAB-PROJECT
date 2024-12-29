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
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import application.models.Appointment;

public class DoctorLoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button doctorLoginButton;

    @FXML
    private Button backButton;

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    // Stack for action history
    private Stack<String> actionHistory = new Stack<>();

    // Queue for appointment management
    private Queue<Appointment> appointmentQueue = new LinkedList<>();

    // LinkedList for managing patient records
    private LinkedList<Appointment> patientRecords = new LinkedList<>();

    public void initialize() {
        backButton.setText("\u2190");
        
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Check if email and password match
        if (email.equals("doctor123@gmail.com") && password.equals("doctor123")) {
            // Add action to stack (login attempt)
            actionHistory.push("Doctor Login Successful: " + email);
            
            // Load Doctor dashboard
            loadDoctorDashboard();
        } else {
            // Add action to stack (failed login attempt)
            actionHistory.push("Doctor Login Failed: " + email);
            showAlert("Error", "Invalid email or password.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        // Apply fade-out effect to the current scene
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

    private void loadDoctorDashboard() {
        try {
            // Load the doctor dashboard FXML
            Parent dashboardRoot = FXMLLoader.load(getClass().getResource("doctor.fxml"));
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(dashboardRoot);

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), currentStage.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                currentStage.setScene(scene);

                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), dashboardRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });

            fadeOut.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Stack Example: View the most recent action
    @FXML
    private void showLoginActionHistory() {
        if (!actionHistory.isEmpty()) {
            String lastAction = actionHistory.peek();
            showAlert("Action History", "Last action: " + lastAction);
        } else {
            showAlert("Action History", "No actions in history.");
        }
    }

    // Queue Example: Add an appointment to the queue
    @FXML
    private void addAppointmentToQueue(Appointment appointment) {
        appointmentQueue.add(appointment);
        showAlert("Appointment Added", "Appointment added to the queue.");
    }

    // Queue Example: Serve the next appointment in the queue
    @FXML
    private void serveNextAppointment() {
        if (!appointmentQueue.isEmpty()) {
            Appointment nextAppointment = appointmentQueue.poll();
            showAlert("Next Appointment", "Serving appointment: " + nextAppointment.getName());
        } else {
            showAlert("Queue Empty", "No appointments in the queue.");
        }
    }

    // LinkedList Example: Add a patient record
    @FXML
    private void addPatientRecord(Appointment appointment) {
        patientRecords.add(appointment);
        showAlert("Patient Record Added", "Patient record added.");
    }

    // LinkedList Example: View patient records
    @FXML
    private void viewPatientRecords() {
        if (!patientRecords.isEmpty()) {
            StringBuilder records = new StringBuilder("Patient Records:\n");
            for (Appointment record : patientRecords) {
                records.append(record.getName()).append(" - ").append(record.getDisease()).append("\n");
            }
            showAlert("Patient Records", records.toString());
        } else {
            showAlert("No Records", "No patient records found.");
        }
    }

    @FXML
    private void handleBackMouseEnter(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.HAND);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
        backButton.setStyle("-fx-background-color: #38e4c5; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
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
}
