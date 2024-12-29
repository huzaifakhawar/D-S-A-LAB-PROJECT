package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/hospital"; // Replace with your database URL
    private static final String USER = "root"; // Replace with your database username
    private static final String PASSWORD = "S@fyan82"; // Replace with your database password

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                throw new SQLException("Failed to connect to the database", e);
            }
        }
        return connection;
    }
}
