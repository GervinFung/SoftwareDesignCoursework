package frontend;

import backend.Controller;

public final class BookingApp {

    private final ConsoleUI consoleUI;

    private BookingApp() {
        this.consoleUI = new ConsoleUI(new Controller());
    }

    public static void main(final String[] args) {
        new BookingApp().consoleUI.startProcess();
    }
}