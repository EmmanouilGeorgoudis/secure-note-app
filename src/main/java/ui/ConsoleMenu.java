package ui;

import model.User;
import service.AuthService;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final AuthService service = new AuthService();

    public void start() {
        boolean running = true;

        while(running) {
            System.out.println("----Secure Note----");
            System.out.println("1. Register User");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void register() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        boolean success = service.register(username, password);

        if (success) {
            System.out.println("User registered successfully");
        } else {
            System.out.println("Username or Password is incorrect");
        }
    }

    //Jag arbetar efter lärarens princip/arkitekur. Login sammarbetar med service som i sin tur hämtar info från db
    //via repository klassen.

    private void login() {
        System.out.println("Username:");
        String username = scanner.nextLine().trim();

        System.out.println("Password:");
        String password = scanner.nextLine().trim();

        User user = service.login(username, password);
        if (user != null) {
            String role = user.getRole().toString().toLowerCase();
            System.out.println("You're " + role);
            userMenu(user);
        } else {
            System.out.println("fail");
        }
    }

    private void userMenu(User user) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Create note");
            System.out.println("2. Se/Hantera anteckningar");
            System.out.println("3. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createNote(user);
                case "2" -> showNoteTitles(user);
                case "3" -> inMenu = false;
                default -> System.out.println("Felaktigt val");
            }
        }
    }

    private void createNote(User user) {

        System.out.println("Write your note title: ");
        String title = scanner.nextLine();

        System.out.println("Write your note: ");
        String content = scanner.nextLine();

        if (service.createNote(user, title, content)) {
            System.out.println("Notes saved!");
        } else {
            System.out.println("Something went wrong.");
        }
    }

    public void updateNote(int noteId, String newTitle, String newContent) {
        repository.updateNote(noteId, newTitle, newContent);
    }

    public List<String> showNoteTitles(User user) {
        return repository.getNotesByUserId(userId);
    }
}