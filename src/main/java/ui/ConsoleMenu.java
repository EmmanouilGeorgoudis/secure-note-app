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
            System.out.println("2. Manage notes");
            System.out.println("3. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createNote(user);
                case "2" -> manageNotes(user);
                case "3" -> inMenu = false;
                default -> System.out.println("wrong");
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
                System.out.println("You have no notes yet.");
                return;
            }

            System.out.println("\n*** YOUR NOTES ***");
            for (int i = 0; i < userNotes.size(); i++) {
                System.out.println((i + 1) + ". " + userNotes.get(i).getTitle());
            }
            System.out.println("0. Exit");
            System.out.println("Choose one note to manage: ");

            Note selectedNote = new Note();
            int choice = scanner.nextInt();
            if (choice == 0) {
                inNotes = false;
            } else if (choice > 0 && choice <= userNotes.size()) {
                selectedNote = userNotes.get(choice - 1);
            }

            System.out.println("Actual title: " + selectedNote.getTitle());
            System.out.print("New title (or press ENTER to keep the old): ");
            String newTitle = scanner.nextLine();

            System.out.println("New text (or press ENTER to keep the old: ");
            String newContent = scanner.nextLine();

            if (service.updateNote(selectedNote, newTitle, newContent)) {
                System.out.println("New note updated!");
            } else {
                System.out.println("Failed");
            }
        }
    }
}