package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/secure_notes";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

//    private static String loadPassword() {
//        try {
//            Properties properties = new Properties();
//            InputStream input = DatabaseConnection.class
//                    .getResourceAsStream("db.properties");
//
//            properties.load(input);
//            return properties.getProperty("password");
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}