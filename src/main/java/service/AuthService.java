package service;

import org.mindrot.jbcrypt.BCrypt;
import repository.UserRepository;

import java.util.Scanner;

public class AuthService {

    private final Scanner scanner = new Scanner(System.in);
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

//    //Vem är du?
//    public boolean authentication() {
//        System.out.println("Username: ");
//        String inputUsername = scanner.nextLine();
//
//        System.out.println("Password: ");
//        String inputPassword = scanner.nextLine();
//
//        //Jag kontrollerar och svarar efter inmatning av båda för att inte ge ut mer info än vad man behöver - Yayha
//
//        if (BCrypt.checkpw(inputUsername, storedUsername)
//                && BCrypt.checkpw(inputPassword, storedPassword)) {
//
//            return true;
//        }
//
//        return false;
//    }
//
//    //Vad får du göra?
//    public boolean authorization(String name) {
//
//    }
}