package repository;

import config.DatabaseConnection;

import java.sql.*;

public class UserRepository {

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

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
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