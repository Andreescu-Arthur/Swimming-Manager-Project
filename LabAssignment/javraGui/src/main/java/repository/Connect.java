//package repository;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class Connect {
//    private static final String URL = "jdbc:sqlite:C:/Users/Arthur/Desktop/MyCodes/Programare anu 2 sem 2/AP/Assignment/javraa/javradb.sqlite";
//
//
//    private Connection connection;
//
//    public Connect() {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            this.connection = DriverManager.getConnection(URL);
//        } catch (ClassNotFoundException | SQLException e) {
//            throw new RuntimeException("Error establishing database connection", e);
//        }
//    }
//
//    public Connection getConnection() {
//        return connection;
//    }
//}


package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static final String URL = "jdbc:sqlite:C:/Users/Arthur/Desktop/MyCodes/Programare anu 2 sem 2/AP/Assignment/javraa/javradb.sqlite";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private Connection connection;

    public Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection successful!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error connecting to database!");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new RuntimeException("Database connection is null!");
        }
        return connection;
    }
}
