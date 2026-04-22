package repository;

import config.DatabaseConnection;
import model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    return rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.out.println("Databasfel: " + e.getMessage());
        }

        return null;
    }


/*
* Reflektion: att konvertera till enum i SQL var inte nödvändigt eftersom jag måste oavsett göra det här i metoden
* från String till enum. Det känns dock säkrare i och med att det låses även i SQL och hindrar stavningsfel
*/
    public Role getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role"); //Tydlingen hämtar man även enums som String

                    return Role.valueOf(role.toUpperCase());
                }
            }
        } catch (SQLException e) {
            System.out.println("Databasfel: " + e.getMessage());
        }
        return null;
    }
}