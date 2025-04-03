package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String URL = "C:\\Users\\Arthur\\AppData\\Roaming\\JetBrains\\IntelliJIdea2024.2\\consoles\\db\\b3877056-f6f9-433c-a826-298707e5a05f"; // Change if using MySQL


    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL);
                System.out.println("Connected to the database!");
            } catch (SQLException e) {
                System.out.println("Database connection failed: " + e.getMessage());
            }
        }
        return connection;
    }
}
