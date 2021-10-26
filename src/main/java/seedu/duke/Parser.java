package seedu.duke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class Parser {

    private static final double EPSILON = 0.001;

    /**
     * Parses the user-entered command and additional information/flags.
     *
     * @param userInput the {@link String} containing the user input
     * @return whether the program should continue running after processing the given user input
     */
    public static boolean parseUserInput(String userInput) {
        String[] rawInput = userInput.split(" ", 2);
        String inputCommand = rawInput[0].toLowerCase();
        String inputParams = null;

        if (rawInput.length == 2) {
            inputParams = rawInput[1];
        }

        if (inputCommand.equals("quit")) {
            Ui.goodBye();
            return false;
        } else if (!checkValidCommand(inputCommand)) {
            Storage.getLogger().log(Level.WARNING, "invalid user input");
            Ui.printUnknownCommandError();
            return true;
        } else if (Storage.getListOfTrips().isEmpty()
                && !inputCommand.equals("create")) {
            Storage.getLogger().log(Level.WARNING, "No trip created yet");
            Ui.printNoTripError();
            return true;
        } else if (inputCommand.equals("close")) {
            Storage.setOpenTripAsLastTrip();
            Storage.setLastExpense(null);
            Storage.closeTrip();
            return true;
        }

        handleValidCommands(inputCommand, inputParams);
        return true;
    }

    /**
     * Handles commands entered by the user that are confirmed as valid, and redirects to the appropriate method
     * for further updates.
     *
     * @param inputCommand Valid command executed by the user
     * @param inputParams  Additional information appended to the command by the user
     *                     (inputParams are not checked and may not be valid)
     */
    private static void handleValidCommands(String inputCommand, String inputParams) {
        switch (inputCommand) {
        case "create":
            handleCreateTrip(inputParams);
            break;

        case "edit":
            handleEditTrip(inputParams);
            break;

        case "open":
            handleOpenTrip(inputParams);
            break;

        case "summary":
            handleTripSummary(inputParams);
            break;

        case "view":
            handleViewTrip(inputParams);
            break;

        case "delete":
            handleDeleteTrip(inputParams);
            break;

        case "list":
            executeList();
            break;

        case "expense":
            handleCreateExpense(inputParams);
            break;

        case "edit-exp":
            handleEditExpense(inputParams);
            break;

        case "amount":
            handleAmount(inputParams);
            break;

        case "help":
            Ui.displayHelp();
            break;

        default:
            Ui.printUnknownCommandError();
        }
    }

    private static void handleCreateExpense(String inputParams) {
        try {
            assert inputParams != null;
            executeCreateExpense(inputParams);
        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException e) {
            Ui.printExpenseFormatError();
        }
    }

    private static void handleEditExpense(String inputParams) {
        try {
            assert inputParams != null;
            executeEditExpense(inputParams);
        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException e) {
            Ui.printExpenseFormatError();
        }
    }

    private static void handleAmount(String inputParams) {
        try {
            executeAmount(inputParams);
        } catch (NullPointerException e) {
            Ui.invalidArgForAmount();
        }
    }

    private static void handleViewTrip(String inputParams) {
        try {
            executeView(inputParams);
        } catch (ArrayIndexOutOfBoundsException e) {
            Ui.printFilterFormatError();
        }
    }

    private static void handleDeleteTrip(String inputParams) {
        try {
            assert inputParams != null;
            executeDelete(inputParams);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            Ui.printUnknownTripIndexError();
        } catch (NullPointerException e) {
            Ui.emptyArgForDeleteCommand();
        }
    }

    private static void handleTripSummary(String inputParams) {
        try {
            executeSummary(inputParams);
        } catch (ArrayIndexOutOfBoundsException e) {
            Ui.printUnknownTripIndexError();
        }
    }

    private static void handleOpenTrip(String inputParams) {
        try {
            assert inputParams != null;
            executeOpen(inputParams);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            Ui.printSingleUnknownTripIndexError();
            System.out.println();
        } catch (NullPointerException e) {
            Ui.emptyArgForOpenCommand();
        }
    }

    private static void handleEditTrip(String inputParams) {
        try {
            assert inputParams != null;
            executeEditTrip(inputParams);
        } catch (NumberFormatException | IndexOutOfBoundsException | NullPointerException e) {
            Ui.printUnknownTripIndexError();
        }
    }

    private static void handleCreateTrip(String inputParams) {
        try {
            assert inputParams != null;
            executeCreate(inputParams);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            Ui.printCreateFormatError();
        }
    }

    private static void executeCreate(String indexAsString) {
        String[] newTripInfo = indexAsString.split(" ", 5);
        Trip newTrip = new Trip(newTripInfo);
        Storage.getListOfTrips().add(newTrip);
        System.out.println("Your trip to " + newTrip.getLocation() + " on "
                + newTrip.getDateOfTripString() + " has been successfully added!");
        Storage.setLastTrip(newTrip);
    }

    private static void executeEditTrip(String inputDescription) {
        String[] tripToEditInfo = inputDescription.split(" ", 2);
        assert tripToEditInfo[1] != null;
        String attributesToEdit = tripToEditInfo[1];
        Trip tripToEdit;
        if (tripToEditInfo[0].equals("last")) {
            tripToEdit = Storage.getLastTrip();
            if (tripToEdit == null) {
                Ui.printNoLastTripError();
                return;
            }
        } else {
            int indexToEdit = Integer.parseInt(tripToEditInfo[0]) - 1;
            tripToEdit = Storage.getListOfTrips().get(indexToEdit);
            Storage.setLastTrip(tripToEdit);
        }
        editTripPerAttribute(tripToEdit, attributesToEdit);
    }

    //assumes that listOfTrips have at least 1 trip
    private static void executeOpen(String indexAsString) {
        int indexToGet = Integer.parseInt(indexAsString) - 1;
        Storage.setOpenTrip(Storage.getListOfTrips().get(indexToGet));
        Ui.printOpenTripMessage(Storage.getOpenTrip());
        Storage.setOpenTripAsLastTrip();
    }

    private static void executeSummary(String inputParams) {
        Trip currentTrip = Storage.getOpenTrip();
        Storage.setOpenTripAsLastTrip();
        if (inputParams == null) {
            //list everybody's expense summary
            for (Person p : currentTrip.getListOfPersons()) {
                currentTrip.getIndividualExpenseSummary(p);
                System.out.println();
            }
        } else {
            //list only 1 person, if exists
            try {
                //returns null if no such person
                Person personToView = getValidPersonInTripFromString(inputParams, currentTrip);
                if (personToView != null) {
                    currentTrip.getIndividualExpenseSummary(personToView);
                } else {
                    Ui.printNoPersonFound(inputParams);
                    Ui.printSummaryFormatError();
                }

            } catch (IndexOutOfBoundsException e) {
                Ui.printNoExpensesError();
            }
        }

    }

    private static void executeView(String inputParams) {
        Trip openTrip = Storage.getOpenTrip();
        Storage.setOpenTripAsLastTrip();
        if (inputParams == null) {
            openTrip.viewAllExpenses();
        } else {
            String[] paramString = inputParams.split(" ", 3);
            String secondCommand = paramString[0];
            String expenseCategory = paramString[1];
            String expenseAttribute = paramString[2];
            if (secondCommand.equals("filter")) {
                try {
                    openTrip.getFilteredExpenses(expenseCategory, expenseAttribute);
                } catch (IndexOutOfBoundsException e) {
                    Ui.printNoExpensesError();
                }
            } else if (secondCommand.equals("index")) {
                String expenseIndex = inputParams.split(" ", 2)[1];
                try {
                    int index = Integer.parseInt(expenseIndex);
                    System.out.println(openTrip.getExpenseAtIndex(index));
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    Ui.printUnknownExpenseIndexError();
                }

            }
        }

    }

    private static void executeDelete(String inputParams) {
        int index = Integer.parseInt(inputParams) - 1;
        if (!Storage.checkOpenTrip()) {
            executeDeleteTrip(index);
        } else {
            executeDeleteExpense(index);
        }
    }

    private static void executeDeleteExpense(int expenseIndex) {
        try {
            Trip currentTrip = Storage.getOpenTrip();
            Expense expenseToDelete = currentTrip.getListOfExpenses().get(expenseIndex);
            Double expenseAmount = expenseToDelete.getAmountSpent();
            correctBalances(expenseToDelete);
            currentTrip.removeExpense(expenseIndex);
            Ui.printDeleteExpenseSuccessful(expenseAmount);
        } catch (IndexOutOfBoundsException e) {
            Ui.printUnknownExpenseIndexError();
        }
        Storage.setLastExpense(null);
    }

    private static void correctBalances(Expense expense) {
        Person payer = expense.getPayer();
        for (Person person : expense.getPersonsList()) {
            if (person == payer) {
                payer.setMoneyOwed(payer, -expense.getAmountSplit().get(person.getName()));
                continue;
            }
            payer.setMoneyOwed(person, -expense.getAmountSplit().get(person.getName()));
            person.setMoneyOwed(payer, expense.getAmountSplit().get(person.getName()));
            person.setMoneyOwed(person, -expense.getAmountSplit().get(person.getName()));
        }
    }

    private static void executeDeleteTrip(int tripIndex) {
        ArrayList<Trip> listOfTrips = Storage.getListOfTrips();
        try {
            String tripLocation = listOfTrips.get(tripIndex).getLocation();
            String tripDate = listOfTrips.get(tripIndex).getDateOfTripString();
            listOfTrips.remove(tripIndex);
            Ui.printDeleteTripSuccessful(tripLocation, tripDate);
        } catch (IndexOutOfBoundsException e) {
            Ui.printUnknownTripIndexError();
        }
        Storage.setLastTrip(null);

    }

    private static void executeList() {
        if (!Storage.checkOpenTrip()) {
            Ui.printAllTrips();
        } else {
            Ui.printExpensesInList(Storage.getOpenTrip().getListOfExpenses());
        }
    }

    private static void executeCreateExpense(String inputDescription) {
        Trip currTrip = Storage.getOpenTrip();
        assert Storage.checkOpenTrip();
        Expense newExpense = new Expense(inputDescription);
        currTrip.addExpense(newExpense);
        Storage.setLastExpense(newExpense);
        Ui.printExpenseAddedSuccess();
    }

    private static void executeEditExpense(String inputDescription) {
        //TODO: add edit expense code (for override of exchange rate using manual local currency)
    }

    //@@author joshualeeky
    protected static void updateOnePersonSpending(Expense expense, Person person) {
        person.setMoneyOwed(person, expense.getAmountSpent());
        expense.setPayer(person);
        expense.setAmountSplit(person, expense.getAmountSpent());
    }

    protected static void updateIndividualSpending(Expense expense) {
        boolean hasLogged = false;
        boolean shouldContinue = true;

        Ui.printGetPersonPaid();
        String input = Storage.getScanner().nextLine().strip();
        Person payer = getValidPersonInExpenseFromString(input, expense);
        if (payer != null) {
            expense.setPayer(payer);
            HashMap<Person, Double> amountBeingPaid = new HashMap<>();
            Ui.equalSplitPrompt();
            double total = 0.0;
            for (Person person : expense.getPersonsList()) {
                double amountRemaining = expense.getAmountSpent() - total;
                if (Math.abs(amountRemaining) < EPSILON) {
                    if (!hasLogged) {
                        shouldContinue = getUserToConfirm();
                    }

                    if (shouldContinue) {
                        amountBeingPaid.put(person, 0d);
                        hasLogged = manageLog(hasLogged);
                        continue;
                    } else {
                        updateIndividualSpending(expense);
                        return;
                    }
                }
                Ui.printHowMuchDidPersonSpend(person.getName(), amountRemaining);
                String amountString = Storage.getScanner().nextLine().strip();
                if (amountString.equalsIgnoreCase("equal") && amountBeingPaid.isEmpty()) {
                    assignEqualAmounts(payer, expense, amountBeingPaid);
                    return;
                } else {
                    try {
                        double amount = Double.parseDouble(String.format(
                                amountString, Storage.getOpenTrip().getForeignCurrencyFormat()));
                        total += amount;
                        if (total > expense.getAmountSpent()) {
                            Ui.printIncorrectAmount(expense.getAmountSpent());
                            updateIndividualSpending(expense);
                            return;
                        } else {
                            amountBeingPaid.put(person, amount);
                        }
                    } catch (NumberFormatException e) {
                        Ui.argNotNumber();
                        updateIndividualSpending(expense);
                        return;
                    }
                }
            }
            if (total < expense.getAmountSpent()) {
                Ui.printIncorrectAmount(expense.getAmountSpent());
                updateIndividualSpending(expense);
            } else {
                assignAmounts(payer, expense, amountBeingPaid);
            }
        } else {
            Ui.printPersonNotInExpense();
            Ui.printPeopleInvolved(expense.getPersonsList());
            updateIndividualSpending(expense);
        }
    }

    //@@author lixiyuan416
    private static boolean manageLog(boolean isLogDisplayed) {
        if (!isLogDisplayed) {
            Storage.getLogger().log(Level.INFO, "Some people were allocated 0 for this expense split");
            Ui.autoAssignIndividualSpending();
            isLogDisplayed = true;
        }
        return isLogDisplayed;
    }

    private static boolean getUserToConfirm() {
        Ui.askUserToConfirm();
        boolean isValidInput = false;
        boolean doesUserAgree = false;
        while (!isValidInput) {
            String userReply = Storage.getScanner().nextLine();
            if (userReply.equalsIgnoreCase("y")) {
                isValidInput = true;
                doesUserAgree = true;
            } else if (userReply.equalsIgnoreCase("n")) {
                isValidInput = true;
            } else {
                System.out.println("Enter y/n");
            }
        }
        return doesUserAgree;
    }
    //@@author

    private static Person getValidPersonInExpenseFromString(String name, Expense expense) {
        for (Person person : expense.getPersonsList()) {
            if (name.equalsIgnoreCase(person.getName())) {
                return person;
            }
        }
        return null;
    }

    private static void assignEqualAmounts(Person payer, Expense expense, HashMap<Person, Double> amountBeingPaid) {
        double total = 0.0;
        double amount = Double.parseDouble(String.format(Storage.getOpenTrip().getForeignCurrencyFormat(),
                (expense.getAmountSpent() / expense.getPersonsList().size())));
        for (Person people : expense.getPersonsList()) {
            amountBeingPaid.put(people, amount);
            total += amount;
        }
        //This will cause payer to bear the deficit or surplus
        if (total != expense.getAmountSpent()) {
            double payerAmount = amountBeingPaid.get(payer) + (expense.getAmountSpent() - total);
            amountBeingPaid.put(payer, payerAmount);
        }
        assignAmounts(payer, expense, amountBeingPaid);
    }

    private static void assignAmounts(Person payer, Expense expense, HashMap<Person, Double> amountBeingPaid) {
        for (Person person : expense.getPersonsList()) {
            if (person == payer) {
                person.setMoneyOwed(person, amountBeingPaid.get(person));
                expense.setAmountSplit(person, amountBeingPaid.get(person));
                continue;
            }
            payer.setMoneyOwed(person, amountBeingPaid.get(person));
            person.setMoneyOwed(payer, -amountBeingPaid.get(person));
            person.setMoneyOwed(person, amountBeingPaid.get(person));
            expense.setAmountSplit(person, amountBeingPaid.get(person));
        }
    }

    private static void executeAmount(String inputParams) {
        Trip trip = Storage.getOpenTrip();
        Person toBeChecked = getValidPersonInTripFromString(inputParams, trip);
        if (toBeChecked == null) {
            Ui.invalidArgForAmount();
        } else {
            Ui.printAmount(toBeChecked, trip);
        }
    }

    private static Person getValidPersonInTripFromString(String name, Trip trip) {
        for (Person person : trip.getListOfPersons()) {
            if (name.equalsIgnoreCase(person.getName())) {
                return person;
            }
        }
        return null;
    }
    //@@author


    private static boolean checkValidCommand(String inputCommand) {
        return Storage.getValidCommands().contains(inputCommand);
    }


    private static void editTripPerAttribute(Trip tripToEdit, String attributesToEdit) {
        String[] attributesToEditSplit = attributesToEdit.split("-");
        for (String attributeToEdit : attributesToEditSplit) {
            String[] splitCommandAndData = attributeToEdit.split(" ");
            String data = splitCommandAndData[1];
            switch (splitCommandAndData[0]) {
            /*case "budget":
                tripToEdit.setBudget(data);
                break;*/
            case "location":
                tripToEdit.setLocation(data);
                break;
            case "date":
                tripToEdit.setDateOfTrip(data);
                break;
            case "exchangerate":
                tripToEdit.setExchangeRate(data);
                break;
            //TODO: confirm syntax for input
            case "basecur":
                tripToEdit.setRepaymentCurrency(data);
                break;
            //TODO: confirm syntax for input
            case "paycur":
                tripToEdit.setForeignCurrency(data);
                break;
            case "person":
                //TODO: add edit persons branch
                break;
            default:
                System.out.println(splitCommandAndData[0] + " was not recognised. "
                        + "Please try again after this process is complete");
            }
        }
    }
}
