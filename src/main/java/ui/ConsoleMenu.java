package ui;

import service.AuthService;

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

    public void register() {
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

    public void login() {
        System.out.println("Username:");
        String username = scanner.nextLine();

        System.out.println("Password:");
        String password = scanner.nextLine();

        if (service.isAuthenticated(username, password)) {
            String role = service.getAuthorization(username, password).toString().toLowerCase();
            System.out.println("You're " + role);
        } else {
            System.out.println("fail");
        }
    }
}