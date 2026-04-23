package service;

import model.Role;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import repository.UserRepository;

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

        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt());

        return repository.saveUser(username,hashedPassword, "USER");
    }

    //Vem är du? authentication
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
        boolean checkPw = BCrypt.checkpw(password,user.getPassword());
        if(checkPw){
            return user;
        }
        return null;
    }

    //Vad får du göra? (Ska jag returnerna String eller kanske ett Objekt? t ex Admin-objekt eller tom enum?)
    public Role getAuthorization(String username, String password) {
        if (isAuthenticated(username, password)) { //User != null

            return repository.getUserRole(username);
        }
        return null;
    }
}