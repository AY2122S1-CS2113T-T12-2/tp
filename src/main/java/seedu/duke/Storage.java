package seedu.duke;

import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class Storage {

    private static Scanner scanner;

    private static final ArrayList<String> commands = new ArrayList<>(
            Arrays.asList("create", "edit", "summary", "delete", "expense", "quit"));

    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scanner) {
        Storage.scanner = scanner;
    }

    public static ArrayList<String> getCommands() {
        return commands;
    }
}
