package application;

import application.models.Appointment;
import application.models.Doctor;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NurseController {

    @FXML
    private Button logoutButton;
    

    @FXML
    private Button saveAppointmentButton;

    

    @FXML
    private Button deleteButton;
	

    @FXML
    private Button backButton;

    @FXML
    private Button createAppointmentButton;

    @FXML
    private TableView<Appointment> appointmentTable;

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
    private TextField searchField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField diseaseField;

    @FXML
    private TextField timeField;

    @FXML
    private ComboBox<Doctor> doctorComboBox;

    @FXML
    private TextField genderField;

    @FXML
    private TextField priceField;

    

    private ObservableList<Appointment> appointmentList;

    private Appointment selectedAppointment;
    @FXML
    private ComboBox<String> wardComboBox;  // Add ComboBox for wards

    @FXML
    private void initialize() {
        // Setting up the table columns to display data
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        diseaseColumn.setCellValueFactory(new PropertyValueFactory<>("disease"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        wardColumn.setCellValueFactory(new PropertyValueFactory<>("ward"));

        // Fetch and load the appointments and doctors
        fetchAppointments();
        loadDoctors();
        loadWards(); // Load wards into ComboBox

        // Add a listener to the table to update form fields when an appointment is selected
        appointmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedAppointment = newSelection;

                // Make the form fields visible
                nameField.setVisible(true);
                phoneField.setVisible(true);
                diseaseField.setVisible(true);
                timeField.setVisible(true);
                doctorComboBox.setVisible(true);
                genderField.setVisible(true);
                priceField.setVisible(true);
                wardComboBox.setVisible(true);

                // Populate form fields with the selected appointment's data
                nameField.setText(selectedAppointment.getName());
                phoneField.setText(selectedAppointment.getPhone());
                diseaseField.setText(selectedAppointment.getDisease());
                timeField.setText(selectedAppointment.getTime());
                genderField.setText(selectedAppointment.getGender());
                priceField.setText(selectedAppointment.getPrice());

                // Set the ward in the ComboBox if available
                if (selectedAppointment.getWard() != null) {
                    wardComboBox.getSelectionModel().select(selectedAppointment.getWard());
                }

                // Set the doctor in the ComboBox if available
                if (selectedAppointment.getDoctor() != null) {
                    doctorComboBox.getSelectionModel().select(
                            doctorComboBox.getItems().stream()
                                    .filter(doctor -> doctor.getName().equals(selectedAppointment.getDoctor()))
                                    .findFirst()
                                    .orElse(null)
                    );
                }
            }
        });

        // Set ComboBox to show doctor names
        doctorComboBox.setCellFactory(param -> new ListCell<Doctor>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        doctorComboBox.setButtonCell(new ListCell<Doctor>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private void loadWards() {
        // This is where you can fetch the list of wards from the database or use a predefined set
        ObservableList<String> wards = FXCollections.observableArrayList(
                "Ward A", "Ward B", "Ward C", "Ward D", "Ward E", "Ward F"
        );
        wardComboBox.setItems(wards); // Set wards in the ComboBox
    }
    
    private void fetchAppointments() {
        try {
            List<Appointment> appointments = DatabaseHandler.getInstance().getAllAppointments();
            appointmentList = FXCollections.observableArrayList(appointments);
            appointmentTable.setItems(appointmentList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load appointments.");
        }
    }

    private void loadDoctors() {
        try {
            List<Doctor> doctors = DatabaseHandler.getInstance().getAllDoctors();
            doctorComboBox.getItems().clear(); // Clear existing items if any
            doctorComboBox.getItems().addAll(doctors);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load doctors.");
        }
    }
    @FXML
    private void handleCreateAppointmentButtonAction(ActionEvent event) {
        // Make the form fields visible
        nameField.setVisible(true);
        phoneField.setVisible(true);
        diseaseField.setVisible(true);
        timeField.setVisible(true);
        doctorComboBox.setVisible(true);
        genderField.setVisible(true);
        priceField.setVisible(true);
        wardComboBox.setVisible(true);

        // Make the "Save Appointment" button visible
        createAppointmentButton.setVisible(true);

        // Optionally, you can focus on the first field
        nameField.requestFocus();
    }



    @FXML
    private void handleCreateAppointment(ActionEvent event) {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String disease = diseaseField.getText();
        String time = timeField.getText();
        Doctor doctor = doctorComboBox.getSelectionModel().getSelectedItem();
        String gender = genderField.getText();
        String price = priceField.getText();
        String ward = wardComboBox.getSelectionModel().getSelectedItem();

        // Ensure at least name or phone is filled in
        if (name.isEmpty() && phone.isEmpty()) {
            showAlert("Error", "Please fill in at least the name or phone number.");
            return;
        }

        // Ensure both time and doctor are provided
        if (time.isEmpty()) {
            showAlert("Error", "Please provide a time for the appointment.");
            return;
        }
        if (doctor == null || doctor.getName().equals("N/A")) {
            showAlert("Error", "Please select a doctor.");
            return;
        }

        // Optional: Validate time format if time is entered
        if (!time.isEmpty() && !isValidTimeFormat(time)) {
            showAlert("Error", "Invalid time format. Please enter time in HH:MM AM/PM format.");
            return;
        }

        // Check if an appointment with the same doctor and time already exists
        if (isAppointmentConflict(doctor, time)) {
            showAlert("Error", "Appointments with the same doctor and time already exist.");
            return;
        }

        // Set default values if fields are empty (except for time and doctor)
        disease = disease.isEmpty() ? "N/A" : disease;
        gender = gender.isEmpty() ? "N/A" : gender;
        price = price.isEmpty() ? "N/A" : price;
        ward = ward.isEmpty() ? "N/A" : ward;

        try {
            // Assuming DatabaseHandler handles ID generation
            int id = DatabaseHandler.getInstance().getNewAppointmentId();  // Fetch or generate a unique ID for the appointment
            Appointment newAppointment = new Appointment(
                    id,
                    name,
                    phone,
                    disease,         // Use "N/A" if empty
                    time,            // Use the entered time
                    doctor.getName(), // Ensure the doctor is selected
                    gender,          // Use "N/A" if empty
                    price,           // Use "N/A" if empty
                    ward             // Use "N/A" if empty
            );

            // Save the appointment to the database
            DatabaseHandler.getInstance().saveAppointmentToPatientProfile(newAppointment);
            showAlert("Success", "Appointment created successfully.");
            fetchAppointments(); // Refresh the appointment list
            clearForm(); // Clear the form after success
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to create appointment.");
        }
    }


    @FXML
    private void handleUpdateAppointment(ActionEvent event) {
        if (selectedAppointment == null) {
            showAlert("Error", "No appointment selected.");
            return;
        }

        // Get the new values from the input fields
        String name = nameField.getText();
        String phone = phoneField.getText();
        String disease = diseaseField.getText();
        String time = timeField.getText();
        Doctor doctor = doctorComboBox.getSelectionModel().getSelectedItem();
        String gender = genderField.getText();
        String price = priceField.getText();
        String ward = wardComboBox.getSelectionModel().getSelectedItem();  // Get selected ward

        // Validate required fields (e.g., name or phone)
        if (name.isEmpty() && phone.isEmpty()) {
            showAlert("Error", "Please fill in at least the name or phone number.");
            return;
        }

        // Optional: Validate time format if time is entered
        if (!time.isEmpty() && !isValidTimeFormat(time)) {
            showAlert("Error", "Invalid time format. Please enter time in HH:MM AM/PM format.");
            return;
        }

        // Set default values if fields are empty
        disease = disease.isEmpty() ? "N/A" : disease;
        time = time.isEmpty() ? "N/A" : time;
        String doctorName = doctor != null ? doctor.getName() : "N/A";
        gender = gender.isEmpty() ? "N/A" : gender;
        price = price.isEmpty() ? "N/A" : price;
        ward = (ward == null || ward.isEmpty()) ? "N/A" : ward;  // Null check for ward field

        // Update only the fields that have changed
        boolean updated = false;

        if (!name.equals(selectedAppointment.getName())) {
            selectedAppointment.setName(name);
            updated = true;
        }
        if (!phone.equals(selectedAppointment.getPhone())) {
            selectedAppointment.setPhone(phone);
            updated = true;
        }
        if (!disease.equals(selectedAppointment.getDisease())) {
            selectedAppointment.setDisease(disease);
            updated = true;
        }
        if (!time.equals(selectedAppointment.getTime())) {
            selectedAppointment.setTime(time);
            updated = true;
        }
        if (doctor != null && !doctor.getName().equals(selectedAppointment.getDoctor())) {
            selectedAppointment.setDoctor(doctorName);
            updated = true;
        }
        if (!gender.equals(selectedAppointment.getGender())) {
            selectedAppointment.setGender(gender);
            updated = true;
        }
        if (!price.equals(selectedAppointment.getPrice())) {
            selectedAppointment.setPrice(price);
            updated = true;
        }
        if (!ward.equals(selectedAppointment.getWard())) {
            selectedAppointment.setWard(ward);
            updated = true;
        }

        if (!updated) {
            showAlert("Info", "No changes detected.");
            return; // No fields were updated, so exit early
        }

        try {
            // Update the appointment in the database
            DatabaseHandler.getInstance().updateAppointment(selectedAppointment);
            showAlert("Success", "Appointment updated successfully.");
            fetchAppointments(); // Refresh the appointment list
            clearForm(); // Clear the form after success
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update appointment.");
        }
    
    
    
    if (ward != null && !ward.isEmpty()) {
        // Proceed with your logic
    } else {
        // Handle the null or empty case
    }}


    @FXML
    private void handleSearchAppointments(ActionEvent event) {
        String searchName = searchField.getText();
        if (searchName.isEmpty()) {
            showAlert("Error", "Please enter a name to search.");
            return;
        }

        try {
            List<Appointment> appointments = DatabaseHandler.getInstance().getAppointmentsByName(searchName);
            appointmentList = FXCollections.observableArrayList(appointments);
            appointmentTable.setItems(appointmentList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to search appointments.");
        }
    }

    @FXML
    private void handleDeleteAppointment(ActionEvent event) {
        if (selectedAppointment == null) {
            showAlert("Error", "No appointment selected.");
            return;
        }

        try {
            DatabaseHandler.getInstance().deleteAppointment(selectedAppointment);
            appointmentTable.getItems().remove(selectedAppointment);
            showAlert("Success", "Appointment deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete appointment.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Stage stage = (Stage) appointmentTable.getScene().getWindow();
        stage.close();

        try {
            Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load login view.");
        }
    }

    private void clearForm() {
        nameField.clear();
        phoneField.clear();
        diseaseField.clear();
        timeField.clear();
        doctorComboBox.getSelectionModel().clearSelection();
        genderField.clear();
        priceField.clear();
        wardComboBox.getSelectionModel().clearSelection();
        
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private boolean isValidTimeFormat(String time) {
        // Regular expression to match HH:MM AM/PM format
        String timeRegex = "^([1-9]|1[0-2]):([0-5][0-9])\\s(?:AM|PM)$";
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
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
        button.setStyle("-fx-background-color: #07b293; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }
    

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            // Load the main page FXML
            Parent mainPage = FXMLLoader.load(getClass().getResource("dashboard.fxml")); // Ensure correct path
            Scene mainScene = new Scene(mainPage);

            // Get the current stage and set the new scene
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the main page.");
        }
    }
    public void handleLogoutButtonMouseEnter(javafx.scene.input.MouseEvent event) {
    	((javafx.scene.Node) event.getSource()).setCursor(Cursor.HAND);

        // Apply scale transition (zoom in)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
        logoutButton.setStyle("-fx-background-color: #FF1D18; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }
    public void handleLogoutButtonMouseExit(javafx.scene.input.MouseEvent event) {
    	((javafx.scene.Node) event.getSource()).setCursor(Cursor.DEFAULT);

        // Reset scale back to normal size (zoom out)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), (Button) event.getSource());
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
        logoutButton.setStyle("-fx-background-color: #424242; -fx-background-radius: 20; -fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 12pt;");
    }

    private boolean isAppointmentConflict(Doctor doctor, String time) {
        if (doctor == null || time == null || time.isEmpty()) {
            return false;
        }

        // Check if any appointment exists with the same doctor and time
        try {
            List<Appointment> appointments = DatabaseHandler.getInstance().getAllAppointments();
            for (Appointment appointment : appointments) {
                if (appointment.getDoctor().equals(doctor.getName()) && appointment.getTime().equals(time)) {
                    return true; // Conflict found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
