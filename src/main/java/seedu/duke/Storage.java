package seedu.duke;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.logging.Logger;

public class Storage {

    private static final String FILE_PATH = "trips.json";
    private static ArrayList<Trip> listOfTrips = new ArrayList<>();
    private static Trip openTrip = null;
    private static Trip lastTrip = null;
    private static Expense lastExpense = null;

    private static Scanner scanner;
    private static Logger logger;

    private static final ArrayList<String> validCommands = new ArrayList<>(
            Arrays.asList("create", "edit", "view", "open", "list", "summary",
                    "delete", "expense", "quit", "help", "amount", "close"));

    private static final HashMap<String, String[]> availableCurrency = new HashMap<>() {{
            put("USD", new String[] {"$", "%.02f"});
            put("SGD", new String[] {"$", "%.02f"});
            put("AUD", new String[] {"$", "%.02f"});
            put("CAD", new String[] {"$", "%.02f"});
            put("NZD", new String[] {"$", "%.02f"});
            put("EUR", new String[] {"€", "%.02f"});
            put("GBP", new String[] {"£", "%.02f"});
            put("MYR", new String[] {"RM", "%.02f"});
            put("HKD", new String[] {"$", "%.02f"});
            put("THB", new String[] {"฿", "%.02f"});
            put("RUB", new String[] {"₽", "%.02f"});
            put("ZAR", new String[] {"R", "%.02f"});
            put("TRY", new String[] {"₺", "%.02f"});
            put("BRL", new String[] {"R$", "%.02f"});
            put("DKK", new String[] {"Kr.", "%.02f"});
            put("PLN", new String[] {"zł", "%.02f"});
            put("ILS", new String[] {"₪", "%.02f"});
            put("SAR", new String[] {"SR", "%.02f"});
            put("CNY", new String[] {"¥", "%.0f"});
            put("JPY", new String[] {"¥", "%.0f"});
            put("KRW", new String[] {"₩", "%.0f"});
            put("IDR", new String[] {"Rp", "%.0f"});
            put("INR", new String[] {"Rs", "%.0f"});
            put("CHF", new String[] {"SFr.", "%.0f"});
            put("SEK", new String[] {"kr", "%.0f"});
            put("NOK", new String[] {"kr", "%.0f"});
            put("MXN", new String[] {"$", "%.0f"});
            put("TWD", new String[] {"NT$", "%.0f"});
            put("HUF", new String[] {"Ft", "%.0f"});
            put("CLP", new String[] {"$", "%.0f"});
            put("PHP", new String[] {"₱", "%.0f"});
            put("AED", new String[] {"د.إ", "%.0f"});
            put("COP", new String[] {"$", "%.0f"});
            put("RON", new String[] {"lei", "%.0f"});
        }};

    public static HashMap<String, String[]> getAvailableCurrency() {
        return availableCurrency;
    }

    protected static void writeToFile() throws IOException {
        String jsonString = new Gson().toJson(listOfTrips);
        FileStorage.writeToFile(jsonString);
    }

    protected static void readFromFile() {
        try {
            String jsonString = FileStorage.readFromFile();
            Type tripType = new TypeToken<ArrayList<Trip>>(){}.getType();
            listOfTrips = new Gson().fromJson(jsonString, tripType);
        } catch (JsonParseException e) {
            Ui.printJsonParseError();
            askOverwriteOrClose();
        } catch (FileNotFoundException e) {
            Ui.printFileNotFoundError();
            tryCreateNewFile();
        }
    }

    private static void tryCreateNewFile() {
        try {
            FileStorage.newBlankFile();
        } catch (IOException ex) {
            Ui.printCreateFileFailure();
            System.exit(1);
        }
    }

    private static void askOverwriteOrClose() {
        while (true) {
            Ui.printJsonParseUserInputPrompt();
            String input = scanner.nextLine().strip();
            if (input.contains("n")) {
                Ui.goodBye();
                Storage.getLogger().log(Level.WARNING, "JSON Parse failed, user requests program end");
                System.exit(1);
                return;
            } else if (input.contains("y")) {
                tryCreateNewFile();
                return;
            }
        }
    }


    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scanner) {
        Storage.scanner = scanner;
    }

    public static ArrayList<String> getValidCommands() {
        return validCommands;
    }


    public static Trip getOpenTrip() {
        if (openTrip == null) {
            Ui.printNoOpenTripError();
            promptUserForValidTrip();
        }
        return openTrip;
    }

    private static void promptUserForValidTrip() {
        try {
            int tripIndex = Integer.parseInt(scanner.nextLine().strip()) - 1;
            setOpenTrip(listOfTrips.get(tripIndex));
        } catch (NumberFormatException e) {
            Ui.argNotNumber();
        }
    }

    public static void setOpenTripAsLastTrip() {
        lastTrip = openTrip;
    }

    /**
     * Checks if there is an open trip or not.
     *
     * @return true if there is an open trip
     */
    public static boolean checkOpenTrip() {
        return openTrip != null;
    }

    public static void setOpenTrip(Trip openTrip) {
        Storage.openTrip = openTrip;
    }

    public static void closeTrip() {
        Trip tripToBeClosed = openTrip;
        Storage.openTrip = null;
        Ui.printTripClosed(tripToBeClosed);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        Storage.logger = logger;
    }

    public static ArrayList<Trip> getListOfTrips() {
        return listOfTrips;
    }

    public static Trip getLastTrip() {
        return lastTrip;
    }

    public static void setLastTrip(Trip lastTrip) {
        Storage.lastTrip = lastTrip;
    }

    public static Expense getLastExpense() {
        return lastExpense;
    }

    public static void setLastExpense(Expense lastExpense) {
        Storage.lastExpense = lastExpense;
    }


}
