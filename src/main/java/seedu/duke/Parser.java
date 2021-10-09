package seedu.duke;

import java.util.ArrayList;

public class Parser {

    /**
     * Parses the user-entered command and additional information/flags.
     * 
     * @param userInput the {@link String} containing the user input
     * @param listOfTrips the list of trips that the user has added to the program
     * @return whether the program should continue running after processing the given user input
     */
    public static boolean parseUserInput(String userInput, ArrayList<Trip> listOfTrips)
            throws ArrayIndexOutOfBoundsException {
        String[] userInputSplit = userInput.split(" ", 2);
        String inputCommand = userInputSplit[0];
        String inputDescription;
        switch (inputCommand) {
        case "create":
            inputDescription = userInputSplit[1];
            String[] newTripInfo = inputDescription.split(" ");
            Trip newTrip = new Trip(newTripInfo);
            listOfTrips.add(newTrip);
            System.out.println("Your trip to " + newTrip.getLocation() + " on "
                    + newTrip.getDateOfTripString() + " has been successfully added!");
            break;
        case "edit":
            inputDescription = userInputSplit[1];
            String[] tripToEditInfo = inputDescription.split(" ", 2);
            try {
                int indexToEdit = Integer.parseInt(tripToEditInfo[0]) - 1;
                String attributesToEdit = tripToEditInfo[1];
                Trip tripToEdit = listOfTrips.get(indexToEdit);
                editTripPerAttribute(tripToEdit, attributesToEdit);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println("Sorry, no such trip number exists. Please check your trip number and try again.");
            }
            break;
        case "summary":
            inputDescription = userInputSplit[1];
            String[] tripToGetInfo = inputDescription.split(" ", 2);
            String tripNumber = tripToGetInfo[0];
            try {
                int indexToGet = Integer.parseInt(tripToGetInfo[0]) - 1;
                Trip tripToGet = listOfTrips.get(indexToGet);
                tripToGet.printExpensesSummary();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            break;
        case "delete":
            inputDescription = userInputSplit[1];
            deleteTrip(listOfTrips, inputDescription);
            break;
        case "expense":
            inputDescription = userInputSplit[1];
            String[] expenseInfo = inputDescription.split(" ");
            int tripIndex = Integer.parseInt(expenseInfo[0]) - 1;
            Trip currentTrip = listOfTrips.get(tripIndex);
            Double expenseAmount = Double.parseDouble(expenseInfo[1]);
            String expenseDescription = expenseInfo[2];
            String personChained = expenseInfo[3];
            String[] personSplit = personChained.split(",");
            ArrayList<Person> listOfPersonsIncluded = getPersons(tripIndex, personSplit, currentTrip);
            currentTrip.addExpense(new Expense(expenseAmount, expenseDescription, listOfPersonsIncluded));
            break;
        case "quit":
            Ui.goodBye();
            return false;
        default:
            System.out.println("Sorry, we didn't recognize your entry. Please try again, or enter -help "
                    + "to learn more.");
        }
        return true;
    }

    /**
     * Obtains a list of Person objects from array of names of people.
     * @param tripIndex Index of trip to check
     * @param personSplit Names of people involved in expense
     * @return listOfPersons ArrayList containing Person objects included in the expense
     */
    private static ArrayList<Person> getPersons(int tripIndex, String[] personSplit, Trip currentTrip) {
        ArrayList<Person> listOfPersons = new ArrayList<Person>();
        ArrayList<Person> personsInTrip = currentTrip.getListOfPersons();
        Person personToAdd = null;
        for (String nameToCheck : personSplit) {
            for (Person currentPersonInTrip : personsInTrip) {
                String name = currentPersonInTrip.getName();
                if (nameToCheck.equals(name)) {
                    personToAdd = currentPersonInTrip;
                    break;
                }
            }
            if (personToAdd != null) {
                listOfPersons.add(personToAdd);
            }
        }
        return listOfPersons;
    }


    private static void editTripPerAttribute(Trip tripToEdit, String attributesToEdit) {

        String[] attributesToEditSplit = attributesToEdit.split("-");
        for (String attributeToEdit : attributesToEditSplit) {
            String[] splitCommandAndData = attributeToEdit.split(" ");
            String data = splitCommandAndData[1];
            switch (splitCommandAndData[0]) {
            case "budget":
                tripToEdit.setBudget(data);
                break;
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
                System.out.println(splitCommandAndData[0] + "was not recognised. "
                        + "Please try again after this process is complete");
            }

        }

    }

    private static void deleteTrip(ArrayList<Trip> listOfTrips, String s) {
        try {
            int indexToDelete = Integer.parseInt(s);
            Trip tripToRemove = listOfTrips.get(indexToDelete - 1);
            listOfTrips.remove(indexToDelete - 1);
            System.out.println("Your trip to " + tripToRemove.getLocation() + " on "
                    + tripToRemove.getDateOfTripString() + " has been successfully removed");
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Sorry, no such trip number exists. Please check your trip number and try again.");
        }
    }

    /**
     * Splits the user-entered {@link String} of people involved in a trip into a String array.
     * 
     * @param peopleChained String of all persons involved in the trip
     * @return {@link String} array, each element of the array being a person involved in the trip
     */
    public static ArrayList<Person> splitPeople(String peopleChained) {

        ArrayList<Person> listOfPeople = new ArrayList<>();
        String[] peopleSplit = peopleChained.split(",");
        for (int i = 0; i < peopleSplit.length; i++) {
            Person person = new Person(peopleSplit[i], (i == 0));
            listOfPeople.add(person);
        }
        return listOfPeople;

    }
}
