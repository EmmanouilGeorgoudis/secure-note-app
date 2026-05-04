package ui;

import model.Role;
import model.User;
import service.AuthService;

import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final AuthService service = new AuthService();
    private final MenuActions actions = new MenuActions(scanner, service);

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("--- SECURE NOTE ---");
            System.out.println("1. Register User");
            System.out.println("2. Login");
            System.out.println("0. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> register();
                case "2" -> login();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void register() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine().trim();

        System.out.println("Enter your password:");
        String password = scanner.nextLine().trim();

        boolean success = service.register(username, password);

        if (success) {
            System.out.println("User registered successfully");
        } else {
            System.out.println("Could not register user");
        }
    }

    private void login() {
        System.out.println("Username:");
        String username = scanner.nextLine().trim();

        System.out.println("Password:");
        String password = scanner.nextLine().trim();

        User user = service.login(username, password);

        if (user != null) {
            authorizeUser(user);
        } else {
            System.out.println("Login failed");
        }
    }

    private void authorizeUser(User user) {
        String role = user.getRole().toString().toLowerCase();
        System.out.println("Login successful for " + role + ": ");

        switch (user.getRole()) {
            case SUPERVISOR -> new SupervisorMenu(actions, service).display(user);
            case ADMIN -> new AdminMenu(actions, service).display(user);
            case USER -> new UserMenu(actions).display(user);
        }
    }
}
