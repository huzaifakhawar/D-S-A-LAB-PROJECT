package application;

import application.models.Appointment;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DoctorController {

    @FXML
    private Button backButton;

    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private TableColumn<Appointment, String> nameColumn;

    @FXML
    private TableColumn<Appointment, String> phoneColumn;

    @FXML
    private TableColumn<Appointment, String> diseaseColumn;

    @FXML
    private TableColumn<Appointment, String> timeColumn;

    @FXML
    private TableColumn<Appointment, String> doctorColumn;

    @FXML
    private TableColumn<Appointment, String> genderColumn;

    @FXML
    private Button logoutButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Pane doctorPane;

    @FXML
    private void handleBackMouseEnter(javafx.scene.input.MouseEvent event) {
        ((javafx.scene.Node) event.getSource()).setCursor(Cursor.HAND);

        // Apply scale transition (zoom in)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
        backButton.setStyle("-fx-background-color: #07b293; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
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
    // Initialize method for setting table data
    @FXML
    private void initialize() {
        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        diseaseColumn.setCellValueFactory(new PropertyValueFactory<>("disease"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Fetch appointments
        fetchAppointments();

    }

    @FXML
    private void handleLogout() {
        // Close the current window
        Stage stage = (Stage) appointmentsTable.getScene().getWindow();
        stage.close();

        // Load the dashboard
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = loader.load();
            Stage dashboardStage = new Stage();
            dashboardStage.setScene(new Scene(root));
            dashboardStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load dashboard.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        // Load the "login.fxml" file
        Parent root = FXMLLoader.load(getClass().getResource("doctorlogincontroller.fxml"));
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
    private void handleDeleteAppointment() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            try {
                DatabaseHandler.getInstance().deleteAppointment(selectedAppointment);
                appointmentsTable.getItems().remove(selectedAppointment);
                showAlert("Success", "Appointment deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete appointment.");
            }
        } else {
            showAlert("Error", "Please select an appointment to delete.");
        }
    }

    private void fetchAppointments() {
        try {
            List<Appointment> appointments = DatabaseHandler.getInstance().getAllAppointments();
            appointmentsTable.getItems().clear();
            appointmentsTable.getItems().addAll(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch appointments.");
        }
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
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();

        // Change button background color when mouse enters
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #07b293; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }
    
    public void handleDeleteButtonMouseEnter(javafx.scene.input.MouseEvent event) {
    	((javafx.scene.Node) event.getSource()).setCursor(Cursor.HAND);

        // Apply scale transition (zoom in)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
        deleteButton.setStyle("-fx-background-color: #FF1D18; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }
    public void handleDeleteButtonMouseExit(javafx.scene.input.MouseEvent event) {
    	((javafx.scene.Node) event.getSource()).setCursor(Cursor.DEFAULT);

        // Reset scale back to normal size (zoom out)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
        deleteButton.setStyle("-fx-background-color: #424242; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
