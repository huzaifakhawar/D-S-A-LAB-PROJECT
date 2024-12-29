package application;

import application.models.Appointment;
import application.models.Doctor;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminController extends Application {

    @FXML
    private VBox managePatientsPane;

    @FXML
    private VBox manageStaffPane;

    @FXML
    private HBox nameInputPane;
    

    @FXML
    private Button backButton;
    
    
    @FXML
    private Button deleteButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private TableView<Doctor> doctorsTable;

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
    private TableColumn<Appointment, String> priceColumn;
    @FXML
    private TableColumn<Appointment, String> wardColumn;

    @FXML
    private TableColumn<Doctor, String> doctorNameColumn;

    @FXML
    private ComboBox<String> wardComboBox;

    private final DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

    @FXML
    public void initialize() {
        // Hide the managePatientsPane, manageStaffPane, and nameInputPane initially
        managePatientsPane.setVisible(false);
        manageStaffPane.setVisible(false);
        nameInputPane.setVisible(false);

        // Initialize the columns for the appointments table
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        diseaseColumn.setCellValueFactory(cellData -> cellData.getValue().diseaseProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        doctorColumn.setCellValueFactory(cellData -> cellData.getValue().doctorProperty());
        genderColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        wardColumn.setCellValueFactory(cellData -> cellData.getValue().wardProperty());

        // Initialize the columns for the doctors table
        doctorNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        // Populate the wardComboBox with example ward names
        wardComboBox.setItems(FXCollections.observableArrayList("Ward A", "Ward B", "Ward C"));
    }

    @FXML
    private void handleManagePatients() {
        // Display the managePatientsPane when "Manage Patients" button is clicked
        managePatientsPane.setVisible(true);
        manageStaffPane.setVisible(false);
        nameInputPane.setVisible(false);
        loadAppointments();
    }

    @FXML
    private void loadAppointments() {
        try {
            List<Appointment> appointments = databaseHandler.getAllAppointments();
            ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(appointments);
            appointmentsTable.setItems(appointmentList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        button.setStyle("-fx-background-color: #07b293; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        // Load the "login.fxml" file
        Parent root = FXMLLoader.load(getClass().getResource("adminlogin.fxml"));
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
        backButton.setStyle("-fx-background-color: #07b293; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
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

        // Revert button background color back to #424242 when mouse exits
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #424242; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    @FXML
    private void handleDeletePatient() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            try {
                databaseHandler.deleteAppointment(selectedAppointment);
                loadAppointments(); // Refresh the table
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Handle case where no appointment is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
        }
    }
    


    @FXML
    private void handleManageStaff() {
        // Display the manageStaffPane when "Manage Staff" button is clicked
        manageStaffPane.setVisible(true);
        managePatientsPane.setVisible(false);
        nameInputPane.setVisible(false);
        loadDoctors();
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
    
    private void applyFadeTransition(javafx.scene.Node node, double fromValue, double toValue, double duration, EventHandler<ActionEvent> onFinished) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), node);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setOnFinished(onFinished);
        fadeTransition.play();
    }

    @FXML
    private void loadDoctors() {
        try {
            List<Doctor> doctors = databaseHandler.getAllDoctors();
            ObservableList<Doctor> doctorList = FXCollections.observableArrayList(doctors);
            doctorsTable.setItems(doctorList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteDoctor() {
        Doctor selectedDoctor = doctorsTable.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            try {
                databaseHandler.deleteDoctor(selectedDoctor);
                loadDoctors(); // Refresh the table
                showAlert("Success", "Doctor deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete doctor.");
            }
        } else {
            // Handle case where no doctor is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a doctor to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleViewReports(ActionEvent event) {
        String patientName = nameTextField.getText().trim();
        if (!patientName.isEmpty()) {
            try {
                List<Appointment> appointments = DatabaseHandler.getInstance().getAppointmentsByName(patientName);
                if (!appointments.isEmpty()) {
                    StringBuilder details = new StringBuilder();
                    for (Appointment appointment : appointments) {
                        details.append("Name: ").append(appointment.getName()).append("\n");
                        details.append("Gender: ").append(appointment.getGender()).append("\n");
                        details.append("Doctor: ").append(appointment.getDoctor()).append("\n");
                        details.append("Disease: ").append(appointment.getDisease()).append("\n");
                        details.append("Appointment Time: ").append(appointment.getTime()).append("\n");
                        details.append("Price: ").append(appointment.getPrice()).append("\n\n");
                    }
                    showAlert("Appointment Details", details.toString());
                } else {
                    showAlert("No Appointments", "No appointments found for " + patientName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to retrieve appointments.");
            }
        } else {
            showAlert("Error", "Please enter your name.");
        }
   
        // Show the nameInputPane when "View Reports" button is clicked
        managePatientsPane.setVisible(false);
        manageStaffPane.setVisible(false);
        nameInputPane.setVisible(true);
        
    }

    @FXML
    private void handleLogout(){// Close the current window
        Stage stage = (Stage) appointmentsTable.getScene().getWindow();
        stage.close();
   
        // Load the dashboard view
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
    private void handleAssignWardForPatients() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        String selectedWard = wardComboBox.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null && selectedWard != null) {
            selectedAppointment.setWard(selectedWard);
            try {
                databaseHandler.updateAppointment(selectedAppointment);
                loadAppointments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Handle case where no appointment or ward is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment and a ward.");
            alert.showAndWait();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("admin_view.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();
    }
}
