package application;

import application.models.Appointment;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class WardAssignmentController {

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
    private TableColumn<Appointment, String> priceColumn;
    @FXML
    private TableColumn<Appointment, String> wardColumn;
    @FXML
    private ComboBox<String> wardComboBox;
    @FXML
    private Button assignWardButton;

    // Using a Map to efficiently access appointments by ID
    private Map<Integer, Appointment> appointmentMap = new HashMap<>();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        diseaseColumn.setCellValueFactory(new PropertyValueFactory<>("disease"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        wardColumn.setCellValueFactory(new PropertyValueFactory<>("ward"));

        wardComboBox.getItems().addAll("Ward 3", "Ward 4");

        fetchAppointments();  // Fetch appointments and store them in Map

        assignWardButton.setDisable(true);

        appointmentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            assignWardButton.setDisable(newSelection == null);
        });
    }

    // Fetch appointments and populate the Map
    private void fetchAppointments() {
        try {
            List<Appointment> appointments = DatabaseHandler.getInstance().getAllAppointments();
            appointmentMap.clear();  // Clear previous data
            for (Appointment appointment : appointments) {
                appointmentMap.put(appointment.getId(), appointment);  // Store appointments in the map
            }

            appointmentsTable.getItems().clear();
            appointmentsTable.getItems().addAll(appointments);  // Add appointments to table view
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch appointments.");
        }
    }

    @FXML
    private void handleAssignWard(ActionEvent event) {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        String selectedWard = wardComboBox.getValue();
        if (selectedAppointment != null && selectedWard != null) {
            assignWard(selectedAppointment, selectedWard);
            fetchAppointments();  // Re-fetch appointments to reflect changes
        } else {
            showAlert("Error", "Please select a patient and a ward.");
        }
    }

    // Use DSA approach to assign the ward by modifying the Map
    private void assignWard(Appointment appointment, String ward) {
        if (appointmentMap.containsKey(appointment.getId())) {
            Appointment selectedAppointment = appointmentMap.get(appointment.getId());
            selectedAppointment.setWard(ward);  // Update the ward in the object

            try {
                DatabaseHandler.getInstance().assignWard(selectedAppointment.getId(), ward);  // Persist to database
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to assign ward.");
            }
        } else {
            showAlert("Error", "Appointment not found.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
