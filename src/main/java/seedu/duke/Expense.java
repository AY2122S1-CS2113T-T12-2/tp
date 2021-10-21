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

    public Expense(Double amountSpent, String category, ArrayList<Person> listOfPersons, String description) {
        this.amountSpent = amountSpent;
        this.description = description;
        this.category = category;
        this.personsList = listOfPersons;
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
                + "Amount Spent: " + String.format("%.02f", this.getAmountSpent())
                + System.lineSeparator()
                + "People involved: " + this.getPersonsList().toString()
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
}
