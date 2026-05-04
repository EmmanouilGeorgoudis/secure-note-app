package ui;

import model.User;
import service.AuthService;

import java.util.List;

public class AdminMenu {

    private final MenuActions actions;
    private final AuthService service;

    public AdminMenu(MenuActions actions, AuthService service) {
        this.actions = actions;
        this.service = service;
    }

    public void display(User admin) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Create note");
            System.out.println("2. Manage notes");
            System.out.println("3. Manage users");
            System.out.println("4. Account settings");
            System.out.println("0. Logout");

            String choice = actions.getScanner().nextLine().trim();

            switch (choice) {
                case "1" -> actions.createNote(admin);
                case "2" -> actions.manageNotes(admin);
                case "3" -> manageUsers(admin);
                case "4" -> {actions.manageAccount(admin);return;}
                case "0" -> inMenu = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void manageUsers(User admin) {
        boolean inMenu = true;

        while (inMenu) {
            List<User> allUsers = service.getUsersForAdmin(admin);

            if (allUsers.isEmpty()) {
                System.out.println("No users found.");
                return;
            } else {
                System.out.println("\n--- ALL REGISTERED USERS ---");
                for (User user : allUsers) {
                    System.out.println("ID: " + user.getId() + " - Username: " + user.getUsername());
                }
            }

            int selectedId = -1;

            System.out.println("Choose a user to manage by Id (or \"0\" for Exit): ");
            String input = actions.getScanner().nextLine().trim();

            try {
                selectedId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number for Id: ");
                continue;
            }

            if (selectedId == 0) {return;}

            int searchId = selectedId;
            User selectedUser = allUsers.stream()
                    .filter(su -> su.getId() == searchId)
                    .findFirst()
                    .orElse(null);

            if (selectedUser != null) {
                System.out.println("\nManaging User: " + selectedUser.getUsername());
                System.out.println("1. Manage User's Notes");
                System.out.println("2. DELETE User Account");
                System.out.println("0. Cancel");

                String action = actions.getScanner().nextLine();
                switch (action) {
                    case "1" -> actions.manageNotes(selectedUser);
                    case "2" -> actions.deleteUser(selectedUser);
                    case "0" -> {}
                    default -> System.out.println("Invalid action.");
                }
            } else {
                System.out.println("User not found.");
            }
        }
    }
}
