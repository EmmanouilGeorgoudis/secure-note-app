package ui;

import model.Role;
import model.User;
import service.AuthService;

import java.util.List;

public class SupervisorMenu {

    private final MenuActions actions;
    private final AuthService service;

    public SupervisorMenu(MenuActions actions, AuthService service) {
        this.actions = actions;
        this.service = service;
    }

    public void display(User superVisor) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n--- SUPERVISOR MENU ---");
            System.out.println("1. List all users & roles");
            System.out.println("2. Change User Role");
            System.out.println("3. Account settings");
            System.out.println("0. Exit");

            String choice = actions.getScanner().nextLine().trim();

            switch (choice) {
                case "1" -> {
                    List<User> allUsers = service.getUsersForSupervisor(superVisor);
                    for (User u : allUsers) {
                        System.out.println("ID: " + u.getId() + " | Name: " + u.getUsername() + " | Role: " + u.getRole());
                    }
                }
                case "2" -> changeRole(superVisor);
                case "3" -> {actions.manageAccount(superVisor);return;}
                case "0" -> inMenu = false;
                default -> System.out.println("Invalid choice.");
            }

        }
    }

    private void changeRole(User supervisor) {
        int userId;
        try {
            System.out.print("Enter ID of user to change role: ");
            userId = Integer.parseInt(actions.getScanner().nextLine());
            if (userId == 0) return;
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
            return;
        }

        System.out.println("Select new role:");
        System.out.println("1. ADMIN");
        System.out.println("2. USER");
        System.out.print("Choice: ");

        String choice = actions.getScanner().nextLine().trim();
        Role newRole;

        switch (choice) {
            case "1" -> newRole = Role.ADMIN;
            case "2" -> newRole = Role.USER;
            default -> {
                System.out.println("Invalid choice. Role update cancelled.");
                return;
            }
        }

        if (service.updateUserRole(supervisor, userId, newRole)) {
            System.out.println("Role updated to " + newRole.name() + " successfully!");
        } else {
            System.out.println("Failed to update role. Check ID or permission.");
        }
    }
}
