package repository;

import config.DatabaseConnection;
import model.Note;
import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    //Jag har valt id som nyckel så att användaren kommer ha möjlighet att byta användarnamn vid uppdaterad version
    public boolean saveNote(int userId, String title, String noteContent) {
        String sql = "INSERT INTO notes (user_id, title, content) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, title);
            statement.setString(3, noteContent);

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateNote(int noteId, String newTitle, String newContent) {
        String sql = "UPDATE notes SET title = ?, content = ?, WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newTitle);
            statement.setString(2, newContent);
            statement.setInt(3, noteId);

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.valueOf(resultSet.getString("role").toUpperCase()));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Note> findNotesByUserId(int userId) {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT id, user_id, title, content FROM notes WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Note note = new Note();
                note.setId(resultSet.getInt("id"));
                note.setUserId(resultSet.getInt("user_id"));
                note.setTitle(resultSet.getString("title"));
                note.setContent(resultSet.getString("content"));

                notes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }


    public boolean saveUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES(?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);

            int rows = statement.executeUpdate();

            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}



/*
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

*/
/*

    public Role getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String roleSQL = rs.getString("role"); //Tydlingen hämtar man även enums som String

                    return Role.valueOf(roleSQL.toUpperCase());
                }
            }
        } catch (SQLException e) {
            System.out.println("Databasfel: " + e.getMessage());
        }
        return null;
    }
}

*/