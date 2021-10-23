package seedu.duke;

import java.util.ArrayList;

public class Ui {

    public static void printPendingCommand() {
        System.out.print("Enter your command: ");
    }

    public static void printWelcome() {
        String logo = System.lineSeparator()
                + "    ____              __  ___     ____             __  " + System.lineSeparator()
                + "   / __ \\____ ___  __/  |/  ___  / __ )____ ______/ /__" + System.lineSeparator()
                + "  / /_/ / __ `/ / / / /|_/ / _ \\/ __  / __ `/ ___/ //_/" + System.lineSeparator()
                + " / ____/ /_/ / /_/ / /  / /  __/ /_/ / /_/ / /__/ ,<   " + System.lineSeparator()
                + "/_/    \\__,_/\\__, /_/  /_/\\___/_____/\\__,_/\\___/_/|_|  " + System.lineSeparator()
                + "            /____/                                     " + System.lineSeparator();
        System.out.println("Welcome to" + logo);
    }

    public static void goodBye() {
        System.out.println("Goodbye!");
    }


    public static String stringMoney(double val) {
        return String.format("%.02f", val);
    }

    public static void printExpenseDetails(Expense e) {
        System.out.println(e);
    }

    public static void printExpensesSummary(Trip t) {
        System.out.println("This is the summary for your " + t.getLocation() + " trip " + t.getDateOfTripString());
        /*System.out.println("Total budget for this trip: " + stringMoney(t.getBudget()));
        System.out.println("Total expenditure so far: " + stringMoney(t.getTotalExpenses()));
        System.out.println("Current budget left for this trip: " + stringMoney(t.getBudgetLeft()));*/
    }

    public static void printFilteredExpenses(Expense e, int index) {
        System.out.println((index + 1) + ". " + e.toString());
    }

    public static void printExpenseAddedSuccess() {
        System.out.println("Your expense has been added successfully");
    }

    public static void printExpensesInList(Expense expense, int index) {
        System.out.println(index + ". " + expense.getDescription() + " | Cost: "
                + stringMoney(expense.getAmountSpent()));
    }

    public static void printOpenTripMessage(Trip trip) {
        System.out.println("You have opened the following trip: "
                + System.lineSeparator()
                + trip.getLocation() + " | " + trip.getDateOfTripString());
    }

    public static void printTripsInList(Trip trip, int index) {
        System.out.println(index + ". " + trip.getLocation() + " | " + trip.getDateOfTripString());
    }

    public static void printCreateFormatError() {
        System.out.println("Please format your inputs as follows: "
                + System.lineSeparator()
                + "create [place] [date] [exchange rate] [people].");
    }

    public static void printExpenseFormatError() {
        System.out.println("Please format your inputs as follows: "
                + System.lineSeparator()
                + "expense [amount] [category] [people] /[description].");
    }

    public static void printFilterFormatError() {
        System.out.println("Please format your inputs as follows: "
                + System.lineSeparator()
                + "view filter [expense-attribute] [attribute-information]");
    }

    /*public static void printBudgetFormatError() {
        System.out.print("Please re-enter your budget as a decimal number (e.g. 2000.00): ");
    }*/

    public static void printExchangeRateFormatError() {
        System.out.print("Please re-enter your exchange rate as a decimal number (e.g. 1.32): ");
    }

    public static void printDateTimeFormatError() {
        System.out.print("Please check that your date-time format is dd-MM-yyyy. "
                + "Please enter the date again: ");
    }

    public static void printUnknownCommandError() {
        System.out.println("Sorry, we didn't recognize your entry. Please try again, or enter help "
                + "to learn more.");
    }

    public static void printSingleUnknownTripIndexError() {
        System.out.println("Please re-enter your trip number. You should only enter a single trip number at a time");
    }

    public static void printUnknownTripIndexError() {
        System.out.println("Sorry, no such trip number exists. Please check your trip number and try again.");
    }

    public static void printUnknownExpenseIndexError() {
        System.out.println("Sorry, no such expense number exists. Please check your expense number and try again.");
    }

    public static void printNoTripError() {
        System.out.println("You have not created a trip yet. Please create a trip using the keyword 'create'.");
    }

    public static void printDeleteTripSuccessful(String tripLocation, String tripDate) {
        System.out.println("Your trip to " + tripLocation + " on "
                + tripDate + " has been successfully removed");
    }

    public static void printDeleteExpenseSuccessful(Double expenseAmount) {
        System.out.println("Your expense of " + stringMoney(expenseAmount) + " has been successfully removed");
    }

    public static void printNoExpensesError() {
        System.out.println("There are no expenses in your trip, please add an expense using the keyword 'expense'.");
    }

    public static void printNoMatchingExpenseError() {
        System.out.println("No matching expenses found.");
    }

    public static void printNoOpenTripError() {
        System.out.println("You have not opened any trip yet. Please open a trip to edit expenses within the trip.");
        printAllTrips();
        System.out.print("Please enter the trip you would like to open: ");
    }

    public static void printAllTrips() {
        System.out.println("List of Trips: ");
        ArrayList<Trip> listOfTrips = Storage.getListOfTrips();
        for (int i = 0; i < listOfTrips.size(); i++) {
            System.out.print("\t");
            System.out.println(i + 1 + ". "
                    + listOfTrips.get(i).getLocation() + " "
                    + listOfTrips.get(i).getDateOfTripString());

        }
    }

    public static void emptyArgForOpenCommand() {
        System.out.println();
        System.out.println("Which trip to open?");
        System.out.println("Syntax: open [trip number]");
        System.out.println("--------------------------");
        printAllTrips();
        System.out.println("--------------------------");

    }

    public static void argNotNumber() {
        System.out.println("Input is not a number");
    }

    public static void emptyArgForDeleteCommand() {
        System.out.println();
        System.out.println("Which trip to delete?");
        System.out.println("Syntax: delete [trip number]");
        System.out.println("---------------------------");
        printAllTrips();
        System.out.println("---------------------------");
    }

    public static void printInvalidDeleteFormatError() {
        System.out.println("Your current format is wrong. Please follow the proper format of 'delete type index'.");
    }

    public static void printGetPersonPaid() {
        System.out.print("Who paid for the expense?: ");
    }

    public static void printHowMuchDidPersonSpend(String name, double amountRemaining) {
        System.out.print("There is $" + stringMoney(amountRemaining) + " left to be assigned."
                + " How much did " + name + " spend?: ");
    }

    public static void printPersonNotInExpense() {
        System.out.println("The person you entered is not in the expense, please try again.");
    }

    public static void printPersonNotInTrip() {
        System.out.println("The person you entered is not in the opened trip, please try again.");
    }

    public static void printAmount(Person person, Trip trip) {
        System.out.println(person.getName() + " spent $" + stringMoney(person.getMoneyOwed().get(person))
                + " on the trip so far");
        for (Person otherPerson : trip.getListOfPersons()) {
            if (otherPerson != person) {
                if (person.getMoneyOwed().get(otherPerson) > 0) {
                    System.out.println(otherPerson.getName() + " owes $"
                            + stringMoney(person.getMoneyOwed().get(otherPerson)) + " to " + person.getName());
                } else if (person.getMoneyOwed().get(otherPerson) < 0) {
                    System.out.println(person.getName() + " owes $"
                            + stringMoney(-person.getMoneyOwed().get(otherPerson)) + " to " + otherPerson.getName());
                } else {
                    System.out.println(person.getName() + " does not owe anything to " + otherPerson.getName());
                }
            }
        }
    }

    public static void printIncorrectAmount(double amount) {
        System.out.println("The amount you have entered is incorrect, it is either too high or low. The total "
                + "of the expense should equal " + amount);
    }

    public static void printPeopleInvolved(ArrayList<Person> personArrayList) {
        System.out.println("These are the people who are involved in the expense: ");
        for (Person person : personArrayList) {
            System.out.println(person.getName() + " ");
        }
    }

    public static void displayHelp() {
        System.out.println("Create a trip to get started!");
        System.out.println("create [place] [date] [exchange rate] [people]");
        System.out.println();
        System.out.println("Type open [trip number] to open your trip");
        System.out.println("While a trip is open, type [expense] to create an expense for that trip");
        System.out.println();
    }

    public static void printInvalidFilterError() {
        System.out.println("Please filter using the following valid filter attributes: "
                + System.lineSeparator()
                + "[category], [description], [payer]");
    }

    public static void printFileNotFoundError() {
        System.out.println("No preloaded data found! We have created a file for you.");
    }

    public static void printJsonParseError() {
        //todo not sure what this should be
        System.out.println("An unexpected error has occurred! Aborting...");
    }

    public static void printNoLastTripError() {
        System.out.println("You may have deleted the most recently modified trip. "
                + "Please try again with the trip number of the trip you wish to edit.");
    }

}
