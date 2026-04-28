package ui;

import model.Note;
import model.Role;
import model.User;
import service.AuthService;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final AuthService service = new AuthService();

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
            case SUPERVISOR -> supervisorMenu(user);
            case ADMIN -> adminMenu(user);
            case USER -> userMenu(user);
        }
    }

    private void userMenu(User user) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Create note");
            System.out.println("2. Manage notes");
            System.out.println("3. Account settings");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createNote(user);
                case "2" -> manageNotes(user);
                case "3" -> {
                    if (manageAccount(user))
                        return;
                }
                case "0" -> inMenu = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void adminMenu(User admin) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Create note");
            System.out.println("2. Manage notes");
            System.out.println("3. Manage users");
            System.out.println("4. Account settings");
            System.out.println("0. Logout");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createNote(admin);
                case "2" -> manageNotes(admin);
                case "3" -> manageUsers(admin);
                case "4" -> {manageAccount(admin);return;}
                case "0" -> inMenu = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void supervisorMenu(User superVisor) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n--- SUPERVISOR MENU ---");
            System.out.println("1. List all users & roles");
            System.out.println("2. Change User Role");
            System.out.println("3. Account settings");
            System.out.println("0. Exit");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    List<User> allUsers = service.getUsersForSupervisor(superVisor);
                    for (User u : allUsers) {
                        System.out.println("ID: " + u.getId() + " | Name: " + u.getUsername() + " | Role: " + u.getRole());
                    }
                }
                case "2" -> changeRole(superVisor);
                case "3" -> {manageAccount(superVisor);return;}
                case "0" -> inMenu = false;
                default -> System.out.println("Invalid choice.");
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

    private void manageNotes(User user) {
        boolean inMenu = true;

        while (inMenu) {
            List<Note> userNotes = service.getNotesForUser(user);

            if (userNotes.isEmpty()) {
                System.out.println("No notes found for " + user.getUsername() + ".");
                return;
            }

            System.out.println("\n--- ALL NOTES FOR: " + user.getUsername() + " ---");
            for (int i = 0; i < userNotes.size(); i++) {
                System.out.println((i + 1) + ". " + userNotes.get(i).getTitle());
            }
            System.out.println("0. Exit");
            System.out.println("Choose note to manage by number: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please use numbers.");
                continue;
            }

            if (choice == 0) return;

            if (choice > 0 && choice <= userNotes.size()) {
                Note selectedNote = userNotes.get(choice - 1);

                System.out.println("\nSelected: " + selectedNote.getTitle());
                System.out.println("1. Update Note");
                System.out.println("2. DELETE Note");
                System.out.println("0. Cancel");
                System.out.print("Choice: ");

                String action = scanner.nextLine();

                switch (action) {
                    case "1" -> updateNote(selectedNote);
                    case "2" -> deleteNote(selectedNote);
                    case "0" -> {}
                    default -> System.out.println("Invalid choice. Try again");
                }
            } else {
                System.out.println("Note not found.");
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
            String input = scanner.nextLine().trim();

            try {
                selectedId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number for Id: ");
                continue;
            }

            if (selectedId == 0) {return;} //la if-satsen här så slipper pipeline under jobba i onödan

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

                String action = scanner.nextLine();
                switch (action) {
                    case "1" -> manageNotes(selectedUser);
                    case "2" -> deleteUser(selectedUser);
                    case "0" -> {}
                    default -> System.out.println("Invalid action.");
                }
            } else {
                System.out.println("User not found.");
            }
        }
    }

    private boolean manageAccount(User user) {
        boolean inAccountMenu = true;

        while (inAccountMenu) {
            System.out.println("\n--- MANAGE ACCOUNT: " + user.getUsername() + " ---");
            System.out.println("1. Update username or/and password");
            System.out.println("2. DELETE my acount");
            System.out.println("0. Exit");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> updateAccount(user);
                case "2" -> {
                    if (deleteUser(user))
                        return true;
                }
                case "0" -> inAccountMenu = false;
                default -> System.out.println("Invalid choice.");
            }
        } return false;
    }

    private void updateNote(Note selectedNote) {
        System.out.println("Actual title: " + selectedNote.getTitle());
        System.out.print("New title (ENTER to keep): ");
        String newTitle = scanner.nextLine();

        System.out.print("New text (ENTER to keep): ");
        String newContent = scanner.nextLine();

        if (service.updateNote(selectedNote, newTitle, newContent)) {
            System.out.println("Updated successfully!");
        }
    }

    private void updateAccount(User user) {

        System.out.println("\nLeave blank and press ENTER to keep current value.");

        System.out.print("New username (Current: " + user.getUsername() + "): ");
        String newUsername = scanner.nextLine().trim();

        System.out.print("New password: ");
        String newPassword = scanner.nextLine().trim();

        if (service.updateAccount(user, newUsername, newPassword)) {
            System.out.println("Account updated successfully!");
            if (!newUsername.isBlank()) user.setUsername(newUsername);
            if (!newPassword.isBlank()) user.setPassword(newPassword);
        } else {
            System.out.println("Update failed.");
        }
    }

    private void deleteNote(Note selectedNote) {
        System.out.print("Delete '" + selectedNote.getTitle() + "'? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            if (service.deleteNote(selectedNote.getId())) {
                System.out.println("Deleted.");
            }
        }
    }

    private boolean deleteUser(User selectedUser) {
        System.out.print("ARE YOU SURE? This will delete user '" + selectedUser.getUsername() + "' and ALL notes! (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            if (service.deleteUser(selectedUser.getId())) {
                System.out.println("Account deleted.");
                return true;
            } else {
                System.out.println("Could not delete user.");
            }
        }
        return false;
    }

    private void changeRole(User supervisor) {

        int userId;
        try {
            System.out.print("Enter ID of user to change role: ");
            userId = Integer.parseInt(scanner.nextLine());
            if (userId == 0) return;
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
            return;
        }

        System.out.println("Select new role:");
        System.out.println("1. ADMIN");
        System.out.println("2. USER");
        System.out.print("Choice: ");

        String choice = scanner.nextLine().trim();
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