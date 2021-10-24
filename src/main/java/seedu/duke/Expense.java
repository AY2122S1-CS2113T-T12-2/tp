package seedu.duke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;


/**
 * Constructor requires a Person class which is the user, amount spent, and a description.
 * printDate prints out a nicely formatted date.
 * getExpenseSummary assumes user pays the bill first, and expense is equally split among his friends.
 */
public class Expense {
    private double amountSpent;
    private String description;
    private ArrayList<Person> personsList;
    private String category;
    private LocalDate date;
    private Person payer;
    private HashMap<Person, Double> amountSplit = new HashMap<>();
    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private double exchangeRate;

    /**
     * Legacy Constructor for {@link Expense} - does not include parsing.
     *
     * @param amountSpent (placeholder)
     * @param category (placeholder)
     * @param listOfPersons (placeholder)
     * @param description (placeholder)
     * @param exchangeRate (placeholder)
     */
    public Expense(Double amountSpent, String category, ArrayList<Person> listOfPersons,
                   String description, double exchangeRate) {
        this.amountSpent = amountSpent;
        this.description = description;
        this.category = category;
        this.personsList = listOfPersons;
        this.exchangeRate = exchangeRate;
    }

    /**
     * Constructor for {@link Expense} class - contains parsing, date prompting and amount assignment.
     *
     * @param inputDescription String of user input to be parsed and assigned to expense attributes
     */
    public Expense(String inputDescription) {
        String[] expenseInfo = inputDescription.split(" ", 3);
        this.amountSpent = Double.parseDouble(expenseInfo[0]);
        this.description = expenseInfo[1].toLowerCase();
        this.personsList = checkValidPersons(expenseInfo[2]);
        this.category = getDescriptionParse(expenseInfo[2]);
        this.exchangeRate = Storage.getOpenTrip().getExchangeRate();
        this.date = prompDate();
        if (personsList.size() == 1) {
            Parser.updateOnePersonSpending(this, personsList.get(0));
        } else {
            Parser.updateIndividualSpending(this);
        }
    }

    private static String getDescriptionParse(String userInput) {
        return userInput.split("/")[1].trim();
    }

    /**
     * Obtains a list of Person objects from array of names of people.
     *
     * @param userInput the input of the user
     * @return listOfPersons ArrayList containing Person objects included in the expense
     */
    private static ArrayList<Person> checkValidPersons(String userInput) {
        String[] listOfPeople = userInput.split("/")[0].split(",");
        ArrayList<Person> validListOfPeople = new ArrayList<>();
        Storage.getLogger().log(Level.INFO, "Checking if names are valid");
        for (String name : listOfPeople) {
            for (Person person : Storage.getOpenTrip().getListOfPersons()) {
                if (name.trim().equalsIgnoreCase(person.getName())) {
                    validListOfPeople.add(person);
                    break;
                }
            }
        }
        return validListOfPeople;
    }

    public void setPayer(Person person) {
        this.payer = person;
    }

    public Person getPayer() {
        return payer;
    }

    public void setAmountSplit(Person person, double amount) {
        amountSplit.put(person, amount);
    }

    public HashMap<Person, Double> getAmountSplit() {
        return amountSplit;
    }

    /**
     * Prompts user for date.
     *
     * @return today's date if user input is an empty string, otherwise keeps prompting user until a valid date is given
     */
    public LocalDate prompDate() {
        Scanner sc = Storage.getScanner();
        System.out.println("Enter date of expense:");
        System.out.println("\tPress enter to use today's date");
        String inputDate = sc.nextLine();
        while (!isDateValid(inputDate)) {
            inputDate = sc.nextLine();
        }
        if (inputDate.isEmpty()) {
            return LocalDate.now();
        }
        return LocalDate.parse(inputDate, pattern);
    }

    private Boolean isDateValid(String date) {
        if (date.isEmpty()) {
            return true;
        }
        try {
            LocalDate.parse(date, pattern);
            return true;
        } catch (DateTimeParseException e) {
            Storage.getLogger().log(Level.INFO, "Invalid date format entered");
            System.out.println("\tPlease enter date as DD-MM-YYYY, or enter nothing to use today's date");
            return false;
        }
    }

    public void addPerson(Person p) {
        personsList.add(p);
    }

    public void printDate() {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        System.out.println(formattedDate);
    }


    //expense details
    @Override
    public String toString() {
        return (this.getDescription()
                + System.lineSeparator()
                + "date: " + this.getDate()
                + System.lineSeparator()
                + "Amount Spent: " + Ui.stringForeignMoney(this.getAmountSpent())
                + System.lineSeparator()
                + "People involved: " + this.getPersonsList().toString()
                + System.lineSeparator()
                + "Payer: " + this.getPayer()
                + System.lineSeparator()
                + "Category: " + this.category);
    }

    //Getters and setters

    public ArrayList<Person> getPersonsList() {
        return personsList;
    }

    public String getCategory() {
        return category;
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(double amountSpent) {
        this.amountSpent = amountSpent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

}
