package application;

import java.io.IOException;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

public class DashboardController {

    // DSA: Using Stack from java.util
    Stack<Integer> stack = new Stack<>();

    // DSA: Using Queue from java.util
    Queue<Integer> queue = new LinkedList<>();

    // DSA: Using LinkedList from java.util
    LinkedList<Integer> linkedList = new LinkedList<>();

    public void initialize() {
        backButton.setText("\u2190");
    }

    @FXML
    private Button backButton;

    @FXML
    private Button doctorButton, nurseButton, patientButton, adminButton, appointmentButton;

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

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        applyFadeTransition(currentStage.getScene().getRoot(), 1.0, 0.0, 0.5, e -> {
            currentStage.setScene(scene);
            applyFadeTransition(root, 0.0, 1.0, 0.5, null);
        });
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
    private void handleDoctorLogin(ActionEvent event) {
        navigateToWithFadeTransition("DoctorLoginController.fxml", event);
    }

    @FXML
    private void handleNurseLogin(ActionEvent event) {
        navigateToWithFadeTransition("NurseLoginController.fxml", event);
    }

    @FXML
    private void handlePatientLogin(ActionEvent event) {
        navigateToWithFadeTransition("patient.fxml", event);
    }

    @FXML
    private void handleAdminLogin(ActionEvent event) {
        navigateToWithFadeTransition("Adminlogin.fxml", event);
    }

    @FXML
    private void handleMakeAppointment(ActionEvent event) {
        navigateToWithFadeTransition("Appointment.fxml", event);
    }

    // Helper method to navigate with fade transition
    private void navigateToWithFadeTransition(String fxmlFileName, ActionEvent event) {
        try {
            Parent targetRoot = FXMLLoader.load(getClass().getResource(fxmlFileName));
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene targetScene = new Scene(targetRoot);
            applyFadeTransition(currentStage.getScene().getRoot(), 1.0, 0.0, 0.5, e -> {
                currentStage.setScene(targetScene);
                applyFadeTransition(targetRoot, 0.0, 1.0, 0.5, null);
            });
        } catch (IOException e) {
            e.printStackTrace();
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
}
