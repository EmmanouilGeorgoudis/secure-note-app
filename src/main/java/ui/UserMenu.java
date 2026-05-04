package ui;

import model.User;

public class UserMenu {

    private final MenuActions actions;

    public UserMenu(MenuActions actions) {
        this.actions = actions;
    }

    public void display(User user) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Create note");
            System.out.println("2. Manage notes");
            System.out.println("3. Account settings");
            System.out.println("0. Logout");

            String choice = actions.getScanner().nextLine();

            switch (choice) {
                case "1" -> actions.createNote(user);
                case "2" -> actions.manageNotes(user);
                case "3" -> {
                    if (actions.manageAccount(user))
                        return;
                }
                case "0" -> inMenu = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
