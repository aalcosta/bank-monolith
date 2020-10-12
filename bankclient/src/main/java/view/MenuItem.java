package view;

class MenuItem {

    String label;
    Runnable action;

    MenuItem(String label) {
        this(label, () -> System.console().printf("No command!!!"));
    }

    MenuItem(String label, Runnable action) {
        this.label = label;
        this.action = action;
    }
}
