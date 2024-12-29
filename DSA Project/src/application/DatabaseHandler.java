package application;

import application.models.Appointment;
import application.models.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
	

    private static final String URL = "jdbc:mysql://localhost:3306/hospital";
    private static final String USER = "root";
    private static final String PASSWORD = "S@fyan82";

    private static DatabaseHandler instance;
    
    

    private DatabaseHandler() {
        // Private constructor to prevent instantiation
        initializeDatabase();
    }

    public static synchronized DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to initialize the database with default values
    private void initializeDatabase() {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();

            // Create doctors table if it doesn't exist
            statement.execute("CREATE TABLE IF NOT EXISTS doctors (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL)");

            // Create patient_appointments table if it doesn't exist
            statement.execute("CREATE TABLE IF NOT EXISTS patient_appointments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "phone VARCHAR(255) NOT NULL, " +
                    "disease VARCHAR(255) NOT NULL, " +
                    "time VARCHAR(255) NOT NULL, " +
                    "doctor_name VARCHAR(255) NOT NULL, " +
                    "gender VARCHAR(255) NOT NULL, " +
                    "price VARCHAR(255) NOT NULL, " +
                    "ward VARCHAR(255))");

            // Insert default doctors if not already present
            insertDoctorIfNotExists(connection, "Dr. Rashid");
            insertDoctorIfNotExists(connection, "Dr. Umar");
            insertDoctorIfNotExists(connection, "Dr. Abdullah");
            insertDoctorIfNotExists(connection, "Dr. ARYAN");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertDoctorIfNotExists(Connection connection, String name) throws SQLException {
        String query = "SELECT COUNT(*) FROM doctors WHERE name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    try (PreparedStatement insertStatement = connection.prepareStatement(
                            "INSERT INTO doctors (name) VALUES (?)")) {
                        insertStatement.setString(1, name);
                        insertStatement.executeUpdate();
                    }
                }
            }
        }
    }

    public boolean isAppointmentExist(String doctorName, String time) throws SQLException {
        String query = "SELECT COUNT(*) FROM patient_appointments WHERE doctor_name = ? AND time = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, doctorName);
            preparedStatement.setString(2, time);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        }
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctors";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Doctor doctor = new Doctor(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
                doctors.add(doctor);
            }
        }
        return doctors;
    }
    
    public int getNewAppointmentId() throws SQLException {
        String query = "SELECT MAX(id) FROM patient_appointments";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                // If there are appointments, return the next ID
                return resultSet.getInt(1) + 1;
            } else {
                // If the table is empty, start with ID 1
                return 1;
            }
        }
    }

    public List<Appointment> getAppointmentsByName(String patientName) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM patient_appointments WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, patientName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Appointment appointment = new Appointment(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("phone"),
                            resultSet.getString("disease"),
                            resultSet.getString("time"),
                            resultSet.getString("doctor_name"),
                            resultSet.getString("gender"),
                            resultSet.getString("price"),
                            resultSet.getString("ward")
                    );
                    appointments.add(appointment);
                }
            }
        }
        return appointments;
    }


    public void deleteDoctor(Doctor doctor) throws SQLException {
        String query = "DELETE FROM doctors WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, doctor.getId());
            statement.executeUpdate();
        }
    }

    // Modified method to accept Appointment object directly
    public void saveAppointmentToPatientProfile(Appointment appointment) throws SQLException {
        if (isAppointmentExist(appointment.getDoctor(), appointment.getTime())) {
            throw new SQLException("Appointment already exists for this doctor at the specified time.");
        }

        String query = "INSERT INTO patient_appointments (name, phone, disease, time, doctor_name, gender, price, ward) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, appointment.getName());
            preparedStatement.setString(2, appointment.getPhone());
            preparedStatement.setString(3, appointment.getDisease());
            preparedStatement.setString(4, appointment.getTime());
            preparedStatement.setString(5, appointment.getDoctor());
            preparedStatement.setString(6, appointment.getGender());
            preparedStatement.setString(7, appointment.getPrice());
            preparedStatement.setString(8, appointment.getWard());  // Use the actual ward value

            preparedStatement.executeUpdate();
        }
    }
    public void saveAppointmentToPatientProfile(String name, String phone, String disease, String time, String doctor, String gender) throws SQLException {
        String query = "INSERT INTO patient_appointments (name, phone, disease, time, doctor_name, gender) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, disease);
            preparedStatement.setString(4, time);
            preparedStatement.setString(5, doctor);
            preparedStatement.setString(6, gender);
            preparedStatement.executeUpdate();
        }
    }
    public boolean appointmentExists(String doctor, String time) throws SQLException {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor = ? AND time = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, doctor);
            statement.setString(2, time);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;  // If count > 0, appointment exists
                }
            }
        }
        return false;
    }




    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM patient_appointments";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("disease"),
                        resultSet.getString("time"),
                        resultSet.getString("doctor_name"),
                        resultSet.getString("gender"),
                        resultSet.getString("price"),
                        resultSet.getString("ward")
                );
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    public void deleteAppointment(Appointment appointment) throws SQLException {
        String query = "DELETE FROM patient_appointments WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, appointment.getId());
            statement.executeUpdate();
        }
    }

    public void updateAppointment(Appointment appointment) throws SQLException {
        String query = "UPDATE patient_appointments SET phone=?, disease=?, time=?, doctor_name=?, gender=?, price=?, ward=? WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, appointment.getPhone());
            statement.setString(2, appointment.getDisease());
            statement.setString(3, appointment.getTime());
            statement.setString(4, appointment.getDoctor());
            statement.setString(5, appointment.getGender());
            statement.setString(6, appointment.getPrice());
            statement.setString(7, appointment.getWard());
            statement.setInt(8, appointment.getId());
            statement.executeUpdate();
        }
    }

    public void assignWard(int appointmentId, String ward) throws SQLException {
        String query = "UPDATE patient_appointments SET ward=? WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ward);
            statement.setInt(2, appointmentId);
            statement.executeUpdate();
        }
    }

}
