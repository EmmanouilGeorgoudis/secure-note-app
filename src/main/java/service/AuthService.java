package service;

import org.mindrot.jbcrypt.BCrypt;
import repository.UserRepository;

import java.util.Scanner;

public class AuthService {

    private final Scanner scanner = new Scanner(System.in);
    private final UserRepository repository = new UserRepository();

    public enum Role {
        USER,
        ADMIN
    }

    public boolean register(String username, String password) {
        if (username == null || username.isBlank()) {
            System.out.println("Username is empty");
            return false;
        }
        if (password == null || password.isBlank()) {
            System.out.println("Password is empty");
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt());

        return repository.saveUser(username,hashedPassword, "USER");
    }

    //Vem är du?
    public boolean authentication(String username, String password) {
        String storedPassword = repository.getPasswordHash(username);

        if (storedPassword == null) {
            return false;
        }
        //Jag kontrollerar och svarar efter inmatning av båda för att inte ge ut mer info än vad man behöver - Yayha

        return BCrypt.checkpw(password, storedPassword);
    }

    //Vad får du göra? (Ska jag returnerna String eller kanske ett Objekt? t ex Admin-objekt eller tom enum?)
    //AI säger att jag inte ska ha static, men jag struntar i det.
    private static Role authorization(String username, String password) {
        return Role.ADMIN;
    }
//    public boolean isAdmin(String username) {
//        String role = repository.getUserRole(username);
//        return "ADMIN".equalsIgnoreCase(role);
//    }
}