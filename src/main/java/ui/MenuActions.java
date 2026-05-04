package ui;

import model.Note;
import model.User;
import service.AuthService;

import java.util.List;
import java.util.Scanner;

public class MenuActions {

    private final Scanner scanner;
    private final AuthService service;

    public MenuActions(Scanner scanner, AuthService service) {
        this.scanner = scanner;
        this.service = service;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void createNote(User user) {
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

    public void manageNotes(User user) {
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

    public boolean manageAccount(User user) {
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

    public boolean deleteUser(User selectedUser) {
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
}
