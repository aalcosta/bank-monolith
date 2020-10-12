package view;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

class Menu {

    private Optional<String> message;

    private List<MenuItem> items = new ArrayList<>();

    public Menu() {
        this.message = Optional.empty();
    }

    public Menu(String message) {
        this.message = Optional.ofNullable(message);
    }

    public Menu addItem(String label) {
        return this.addItem(new MenuItem(label));
    }

    public Menu addItem(String label, Runnable action) {
        return this.addItem(new MenuItem(label, action));
    }

    public Menu addItem(MenuItem menuItem) {
        this.items.add(menuItem);
        return this;
    }

    public void show() {
        Console console = System.console();

        message.ifPresent(msg -> console.printf("\n\n<<<%s>>>\n", msg));
        for (int i = 0; i < items.size(); i++) {
            console.printf("\n%s - %s", i, items.get(i).label);
        }

        Runnable action = null;
        do {
            action = selectAction(console);
        } while (isNull(action));
        action.run();
    }

    private Runnable selectAction(Console console) {
        try {
            String in = console.readLine("\nOption: ");
            return items.get(Integer.parseInt(in)).action;
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            console.printf(" !!!Invalid option!!! ");
            return null;
        }
    }

}
