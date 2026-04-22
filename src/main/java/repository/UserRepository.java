package repository;

import config.DatabaseConnection;

import java.sql.*;

public class UserRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/secure_notes";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    public boolean saveUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES(?, ?, ?)";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);

            int rows = statement.executeUpdate();

            return rows > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public String getPasswordHash(String username) {
        String sql = "SELECT password FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password_hash");
                }
            }
        } catch (SQLException e) {
            System.out.println("Databasfel: " + e.getMessage());
        }

        return null;

    }
}