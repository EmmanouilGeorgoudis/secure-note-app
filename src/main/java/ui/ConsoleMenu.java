package ui;

import model.Note;
import model.User;
import service.AuthService;

import java.util.List;
import java.util.Scanner;


//Till presentationen, jag har gått efter utbildare principer/stil(t ex med booleans i menyer), strukturen i metodordning nedan har en logik, är den bra?
//Reflektion: intressant hur benämning av metoder som betjänar varandra påverkas och visar arkitekturisk struktur såsom
//getNotesForUser i AuthSerivce som blir findNotesByUserId i repository

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final AuthService service = new AuthService();

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("--- SECURE NOTE ---");
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
//      Här tror jag jag ska lägga existsByUsername?
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        boolean success = service.register(username, password);

        if (success) {
            System.out.println("User registered successfully");
        } else {
            System.out.println("Username or Password is incorrect");
        }
    }

    private void login() {
        System.out.println("Username:");
        String username = scanner.nextLine().trim();

        System.out.println("Password:");
        String password = scanner.nextLine().trim();

        User user = service.login(username, password);
        if (user != null) {
            String role = user.getRole().toString().toLowerCase();
            System.out.println("Login successful! Role: " + role);
            switch (user.getRole()) {
                case ADMIN -> adminMenu(user);
                case USER -> userMenu(user);
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private void userMenu(User user) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Create note");
            System.out.println("2. Manage notes");
            System.out.println("3. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createNote(user);
                case "2" -> manageNotes(user);
                case "3" -> inMenu = false;
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
        boolean inNotes = true;

        while (inNotes) {
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
                System.out.print("Action: ");

                String action = scanner.nextLine();

                switch (action) {
                    case "1" -> updateNoteUi(selectedNote);
                    case "2" -> deleteNoteUi(selectedNote);
                    case "0" -> {}
                    default -> System.out.println("Invalid choice. Try again");
                }
            } else {
                System.out.println("Note not found.");
            }
        }
    }

    private void manageUsers(User admin) {
        boolean inAdminMenu = true;

        while (inAdminMenu) {
            List<User> allUsers = service.getAllUsers(admin); //Skapa samma logik uppåt till repo som service.getNotesForUser(user);

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
                    case "2" -> deleteUserUi(selectedUser);
                    case "0" -> {}
                    default -> System.out.println("Invalid action.");
                }
            } else {
                System.out.println("User not found.");
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
            System.out.println("4. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createNote(admin);
                case "2" -> manageNotes(admin);
                case "3" -> manageUsers(admin);
                case "4" -> inMenu = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void updateNoteUi(Note selectedNote) {
        System.out.println("Actual title: " + selectedNote.getTitle());
        System.out.print("New title (ENTER to keep): ");
        String newTitle = scanner.nextLine();

        System.out.print("New text (ENTER to keep): ");
        String newContent = scanner.nextLine();

        if (service.updateNote(selectedNote, newTitle, newContent)) {
            System.out.println("Updated successfully!");
        }
    }

    private void deleteNoteUi(Note selectedNote) {
        System.out.print("Delete '" + selectedNote.getTitle() + "'? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            if (service.deleteNote(selectedNote.getId())) {
                System.out.println("Deleted.");
            }
        }
    }

    private void deleteUserUi(User selectedUser) {
        System.out.print("ARE YOU SURE? This will delete user '" + selectedUser.getUsername() + "' and ALL their notes! (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            if (service.deleteUser(selectedUser.getId())) {
                System.out.println("Deleted.");
            } else {
                System.out.println("Could not delete user.");
            }
        }
    }
}