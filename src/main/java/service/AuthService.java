package service;

import model.Note;
import model.Role;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class AuthService {

    private final UserRepository repository = new UserRepository();

    public boolean register(String username, String password) {

        if (username == null || username.isBlank()) {
            System.out.println("Username is empty");
            return false;
        }
        if (password == null || password.isBlank()) {
            System.out.println("Password is empty");
            return false;
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return repository.saveUser(username, hashedPassword, "USER");
    }

    public User login(String username, String password) {
        if (username == null || username.isBlank()) {
            System.out.println("Username is empty");
            return null;
        }
        if (password == null || password.isBlank()) {
            System.out.println("Password is empty");
            return null;
        }
        User user = repository.findByUsername(username);
        if (user == null) {
            System.out.println("User not found");
            return null;
        }
        boolean checkPw = BCrypt.checkpw(password, user.getPassword());
        if (checkPw) {
            return user;
        }
        return null;
    }

    public boolean createNote(User user, String title, String content) {
        if (content == null || content.isBlank()) {
            return false;
        }
        if (title == null || title.isBlank()) {
            title = "untitled";
        }
        return repository.saveNote(user.getId(), title, content);
    }

    public List<Note> getNotesForUser(User user) {
        List<Note> notesForUser = repository.findNotesByUserId(user.getId());

        return notesForUser;
    }

    public List<User> getAllUsers(User admin) {
        if (admin == null || admin.getRole() != Role.ADMIN) {
            System.out.println("Unarthorized access attempt!");
            return new ArrayList<>();
        }
        return repository.findAllUsers();
    }


    public boolean updateNote(Note oldNote, String inputTitle, String inputContent) {
        String finalTitle = (inputTitle == null || inputTitle.isBlank())
                ? oldNote.getTitle()
                : inputTitle;

        String finalContent = (inputContent == null || inputContent.isBlank())
                ? oldNote.getContent()
                : inputContent;

        return repository.updateNote(oldNote.getId(), finalTitle, finalContent);
    }

//    public boolean deleteNote() {
//        return;
//    }
//    public boolean deleteUser() {
//        return;
//    }

}