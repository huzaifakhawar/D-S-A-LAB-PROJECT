package application;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.models.Appointment;

public class AppointmentController {

    // Data Structures (Linked List, Stack, Queue)
    private LinkedList<Appointment> appointmentsList = new LinkedList<>(); // Linked List for storing appointments
    private Stack<Appointment> appointmentHistory = new Stack<>(); // Stack for undo functionality
    private Queue<Appointment> appointmentQueue = new LinkedList<>(); // Queue for scheduling appointments

    @FXML
    private Button backButton;
    
    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField diseaseField;

    @FXML
    private TextField timeField;

    @FXML
    private ComboBox<String> doctorComboBox;

    @FXML
    private TextField genderField;

    @FXML
    private ComboBox<String> dayComboBox; // Optional, if you want to add a day selection.

    @FXML
    private void initialize() {
        doctorComboBox.setItems(FXCollections.observableArrayList(
                "Dr. Umar", "Dr. ARYAN", "Dr. Rashid", "Dr. Abdullah"));
    }

    @FXML
    private void handleMakeAppointment() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String disease = diseaseField.getText();
        String time = timeField.getText();
        String doctor = doctorComboBox.getValue();
        String gender = genderField.getText();
        String price = "0";  // You can modify this based on your application logic
        String ward = "General";  // You can modify this based on your application logic

        if (name.isEmpty() || phone.isEmpty() || disease.isEmpty() || time.isEmpty() || doctor == null || doctor.isEmpty() || gender.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Validate and format time input
        if (!isValidTimeFormat(time)) {
            showAlert("Error", "Invalid time format. Please enter time in HH:MM AM/PM format.");
            return;
        }

        // Check for appointment conflict
        if (isAppointmentConflict(doctor, time)) {
            showAlert("Error", "Appointment already exists for this doctor at the specified time.");
            return;
        }

        // Generate appointment ID (example, you might want to use a DB or increment it)
        int appointmentId = generateAppointmentId();

        // Create a new Appointment object with all required fields
        Appointment newAppointment = new Appointment(appointmentId, name, phone, disease, time, doctor, gender, price, ward);
        appointmentsList.add(newAppointment); // Add to Linked List
        appointmentQueue.offer(newAppointment); // Add to Queue for scheduling
        appointmentHistory.push(newAppointment); // Add to Stack for undo functionality

        try {
            // Assuming a method to generate appointment ID and store the appointment
            DatabaseHandler.getInstance().saveAppointmentToPatientProfile(name, phone, disease, time, doctor, gender);
            showAlert("Success", "Appointment made successfully.");
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            showAlert("Error", "Failed to make appointment. Please try again.");
            e.printStackTrace();
        }
    }

    // Method to generate a new appointment ID (simple example)
    private int generateAppointmentId() {
        // This could be a database-generated ID or some other logic
        return appointmentsList.size() + 1; // Example: Incremental ID based on list size
    }

    private boolean isValidTimeFormat(String time) {
        // Regular expression to match HH:MM AM/PM format
        String timeRegex = "^([1-9]|1[0-2]):([0-5][0-9])\\s(?:AM|PM)$";
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    private boolean isAppointmentConflict(String doctor, String time) {
        if (doctor == null || time == null || time.isEmpty()) {
            return false;
        }

        // Check if any appointment exists with the same doctor and time
        try {
            List<Appointment> appointments = DatabaseHandler.getInstance().getAllAppointments();
            for (Appointment appointment : appointments) {
                if (appointment.getDoctor().equals(doctor) && appointment.getTime().equals(time)) {
                    return true; // Conflict found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
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
        // Change cursor to hand when mouse enters the button
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.HAND);

        // Apply scale transition (zoom in)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.15);
        scaleTransition.setToY(1.15);
        scaleTransition.play();

        // Change button background color to #004D40 when mouse enters
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

    private void applyFadeTransition(javafx.scene.Node node, double fromValue, double toValue, double duration, EventHandler<ActionEvent> onFinished) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), node);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setOnFinished(onFinished);
        fadeTransition.play();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
