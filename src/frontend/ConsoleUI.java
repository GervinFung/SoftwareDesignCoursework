package frontend;

import backend.NumericValidation;
import backend.booking.Booking;
import backend.item.Item;
import backend.user.User;
import backend.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.sql.Date.valueOf;

public final class ConsoleUI {

    private final Scanner scanner;
    private final Controller controller;
    private final LocalDate localDate;

    public ConsoleUI(final Controller controller) {
        this.scanner = new Scanner(System.in);
        this.controller = controller;
        this.localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    //Menu display
    private int askMainMenuOption() {
        while(true) {
            System.out.println("+-------------------------+");
            System.out.println("| Please choose an option |");
            System.out.println("+-------------------------+");
            System.out.println("| 1. Login                |");
            System.out.println("| 2. Register As Client   |");
            System.out.println("| 3. Exit                 |");
            System.out.println("+-------------------------+");
            System.out.print("Please enter an option: ");
            final String choice = this.scanner.nextLine();
            if (NumericValidation.tryParseIntInRange(choice, 1, 3)) {
                return Integer.parseInt(choice);
            }
            System.out.println("Please enter from 1 to 3 only");
        }
    }

    private void askBookingOption(final Booking booking) {
        while (true) {
            System.out.println("+----------------------------------------+");
            System.out.println("|         Please choose an option        |");
            System.out.println("+----------------------------------------+");
            System.out.println("| 1. Update a Booking (Ticket Date Only) |");
            System.out.println("| 2. Cancel a Booking                    |");
            System.out.println("| 3. Back to Main Menu                   |");
            System.out.println("+----------------------------------------+");
            System.out.print("Enter an option: ");
            switch (this.scanner.nextLine()) {
                case "1":
                    this.updateBooking(booking);
                    return;
                case "2":
                    this.cancelBooking(booking);
                    return;
                case "3":
                    return;
                default:
                    break;
            }
        }
    }

    private int askAdminMenuOption() {
        while(true) {
            System.out.println("+-----------------------------+");
            System.out.println("|   Please choose an option   |");
            System.out.println("+-----------------------------+");
            System.out.println("| 1. Edit Item Information    |");
            System.out.println("| 2. View All Booking Details |");
            System.out.println("| 3. Search Booking Details   |");
            System.out.println("| 4. Log Out                  |");
            System.out.println("+-----------------------------+");
            System.out.print("Enter an option: ");
            final String input = this.scanner.nextLine();
            if (NumericValidation.tryParseIntInRange(input, 1, 4)) {
                return Integer.parseInt(input);
            }
            System.out.println("Please enter from 1 to 4 ONLY");
        }
    }

    private int askClientMenuOption() {
        while(true) {
            System.out.println("+-----------------------------+");
            System.out.println("|   Please choose an option   |");
            System.out.println("+-----------------------------+");
            System.out.println("| 1. Make a Booking           |");
            System.out.println("| 2. View All Booking Details |");
            System.out.println("| 3. Search Booking Details   |");
            System.out.println("| 4. Log Out                  |");
            System.out.println("+-----------------------------+");
            System.out.print("Enter an option: ");
            final String choice = this.scanner.nextLine();
            if (NumericValidation.tryParseIntInRange(choice, 1, 4)) {
                return Integer.parseInt(choice);
            }
            System.out.println("Please enter from 1 to 4 ONLY");
        }
    }

    //edit item info
    private void askAdminItemMenuOption(final Item item) {
        while (true) {
            System.out.println("+-------------------------------------+");
            System.out.println("|        Please choose an option      |");
            System.out.println("+-------------------------------------+");
            System.out.println("| 1. Edit Existing Item Information   |");
            System.out.println("| 2. Delete Existing Item Information |");
            System.out.println("| 3. Back to Previous Menu            |");
            System.out.println("+-------------------------------------+");
            System.out.print("Enter an option: ");
            final String choice = this.scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    this.editItemInformation(item);
                    return;
                }
                case "2" -> {
                    this.deleteItem(item);
                    return;
                }
                case "3" -> {
                    return;
                }
                default -> System.out.println("Please enter from 1 or 2 ONLY");
            }
        }
    }

    private void adminDealWithInfoOption() {
        this.showItems();
        while (true) {
            System.out.println("+------------------------------------+");
            System.out.println("|       Please choose an option      |");
            System.out.println("+------------------------------------+");
            System.out.println("| 1. Enter Item ID to Edit/Delete    |");
            System.out.println("| 2. Enter (Add/add) to add new Item |");
            System.out.println("| 3. Back to Previous Menu           |");
            System.out.println("+------------------------------------+");
            System.out.print("Enter an option: ");
            final String input = this.scanner.nextLine();
            if ("add".equalsIgnoreCase(input)) {
                this.controller.addItem(this.makeNewItem());
                return;
            } else if ("3".equals(input)) {
                return;
            } else {
                try {
                    final Item itemRetrieved = this.controller.searchItemByItemID(input);
                    if (itemRetrieved != null) {
                        this.askAdminItemMenuOption(itemRetrieved);
                        return;
                    }
                    System.out.println("The ID you've entered is invalid");
                } catch (final NumberFormatException e) {
                    System.out.println("The ID you've entered is invalid (Positive Integers ONLY)");
                }
            }
        }
    }

    //edit item info
    private void editItemInformation(Item item) {
        while (true) {
            System.out.println("+-------------------------------------+");
            System.out.println("|        Please choose an option      |");
            System.out.println("+-------------------------------------+");
            System.out.println("| 1. Edit Item Name                   |");
            System.out.println("| 2. Edit Item Description            |");
            System.out.println("| 3. Edit Item Price                  |");
            System.out.println("| 4. Confirm Changes and Back to Menu |");
            System.out.println("+-------------------------------------+");
            System.out.print("Enter an option: ");
            final Item newItem;
            switch (this.scanner.nextLine()) {
                case "1":
                    newItem = new Item(item, this.askItemName());
                    this.controller.updateItem(newItem, item);
                    item = newItem;
                    break;
                case "2":
                    final CharSequence description = this.askItemDescription();
                    newItem = new Item(item, description);
                    this.controller.updateItem(newItem, item);
                    item = newItem;
                    break;
                case "3":
                    newItem = new Item(item, this.askItemPrice());
                    this.controller.updateItem(newItem, item);
                    item = newItem;
                    break;
                case "4":
                    return;
                default:
            }
        }
    }

    private User askUserIDPassword() {
        while (true) {
            System.out.print("Please enter your ID: ");
            final String ID = this.scanner.nextLine();
            System.out.print("Please enter your password: ");
            final String password = this.scanner.nextLine();
            if (NumericValidation.tryParseIntInRange(ID, 0, Integer.MAX_VALUE)) {
                final User user = this.controller.searchUserByIDPassword(ID, password);
                if (user != null) {
                    return user;
                }
            }
            System.out.println("Invalid ID and/or password. Please try again");
        }
    }


    public void startProcess() {
        while (true) {
            final User user;
            switch (this.askMainMenuOption()) {
                case 1:
                    user = this.askUserIDPassword();
                    this.userChoice(user);
                    break;
                case 2:
                    user = this.createNewClient();
                    this.controller.addClient(user);
                    System.out.println("This is your client ID: " + user.getUserID());
                    this.userChoice(user);
                    break;
                case 3:
                    return;
            }
        }
    }


    private void userChoice(final User user) {
        while (true) {
            if (user.isAdmin()) {
                switch (this.askAdminMenuOption()) {
                    case 1:
                        this.adminDealWithInfoOption();
                        break;
                    case 2:
                        this.adminViewBookingDetails();
                        break;
                    case 3:
                        this.adminSearchEngine();
                        break;
                    case 4:
                        return;
                }
            } else {
                final int clientID = user.getUserID();
                switch (this.askClientMenuOption()) {
                    case 1:
                        this.clientMakeBooking(clientID);
                        break;
                    case 2:
                        this.clientViewBookingDetails(clientID);
                        break;
                    case 3:
                        this.clientSearchEngine(clientID);
                        break;
                    case 4:
                        return;
                }
            }
        }
    }

    //add new item
    private String askItemName() {
        while (true) {
            System.out.print("Please enter item name: ");
            final String name = this.scanner.nextLine();
            if (name.isEmpty() || name.isBlank()) {
                System.out.print("Cannot be empty string");
            }
            else {
                if (this.controller.searchSameItemName(name)) {
                    System.out.println("This item name is already taken. Please try another name");
                } else {
                    System.out.print("Confirm this item name? (Press Enter - Confirm || N/n - No)");
                    if (!"n".equalsIgnoreCase(this.scanner.nextLine())) {
                        return name;
                    }
                }
            }
        }
    }

    private String askItemDescription() {
        while (true) {
            System.out.print("Please enter item description: ");
            final String description = this.scanner.nextLine();
            if (description.isEmpty()) {
                System.out.print("Cannot be empty string");
            }
            else {
                if (this.controller.searchSameItemDescription(description)) {
                    System.out.println("This item description is already taken. Please try another description");
                } else {
                    System.out.print("Confirm this item description? (Press Enter - Confirm || N/n - No)");
                    if (!"n".equalsIgnoreCase(this.scanner.nextLine())) {
                        return description;
                    }
                }
            }
        }
    }

    private double askItemPrice() {
        while (true) {
            System.out.print("Please enter item price: ");
            final String price = this.scanner.nextLine();
            if (NumericValidation.tryParseDoubleInRange(price, 0.1, Double.MAX_VALUE)) {
                System.out.print("Confirm this item price? (Press Enter - Confirm || N/n - No)");
                if (!"n".equalsIgnoreCase(this.scanner.nextLine())) {
                    return Double.parseDouble(price);
                }
            } else {
                System.out.println("Please enter positive numbers ONLY");
            }
        }
    }

    private Item makeNewItem() {
        final int newItemID = this.controller.generateNewItemID();
        return new Item(newItemID, this.askItemName(), this.askItemDescription(), this.askItemPrice());
    }

    private void deleteItem(final Item item) {
        this.showItemHeader();
        System.out.println(item.itemDetailsPrettyPrint());
        System.out.println("Please enter (Y/y to confirm delete || Press Enter to deny)");
        if ("y".equalsIgnoreCase(this.scanner.nextLine())) {
            this.controller.deleteItem(item);
        }
    }


    //add Booking
    private void clientMakeBooking(final int clientID) {
        this.showItems();
        final Booking booking = this.clientMakeNewBooking(clientID, this.askDate());
        if (booking != null) {
            this.controller.addBooking(booking);
        }
    }

    private Booking clientMakeNewBooking(final int clientID, final Date ticketDate) {
        final HashMap<Item, Integer> itemsOrdered = new HashMap<>();
        while(true) {
            final Item item = this.askItemID();
            final int quantity = this.askQuantity();
            if (itemsOrdered.containsKey(item)) {
                itemsOrdered.put(item, quantity + itemsOrdered.get(item));
            } else {
                itemsOrdered.put(item, quantity);
            }
            System.out.println(this.allItemsOrdered(itemsOrdered));
            switch (this.askClientContinueBookingOption()) {
                case "2":
                    return new Booking(clientID
                            , itemsOrdered
                            , valueOf(LocalDateTime.now().toLocalDate())
                            , ticketDate
                            , this.controller.generateNewBookingID());
                case "3":
                    return null;
                default:
                    break;
            }
        }
    }

    private int askYear() {
        while (true) {
            System.out.print("Please enter the year of date: ");
            final String year = this.scanner.nextLine();
            if (NumericValidation.tryParseIntInRange(year, this.localDate.getYear(), 9999)) {
                return Integer.parseInt(year);
            }
            System.out.println("Please enter from " + this.localDate.getYear() + " to " + 9999);
        }
    }

    private int askMonth(final int year) {
        final int minMonth = year > this.localDate.getYear() ? 1 : this.localDate.getMonthValue();
        while (true) {
            System.out.print("Please enter the month of date: ");
            final String month = this.scanner.nextLine();
            if (NumericValidation.tryParseIntInRange(month, minMonth, 12)) {
                return Integer.parseInt(month);
            }
            System.out.println("Please enter from " + minMonth + " to " + 12);
        }
    }

    private int askDay(final int minDay, final int maxDay) {
        while (true) {
            System.out.print("Please enter the day of date: ");
            final String day = this.scanner.nextLine();
            if (NumericValidation.tryParseIntInRange(day, minDay, maxDay)) {
                return Integer.parseInt(day);
            }
            System.out.println("Please enter from " + minDay + " to " + maxDay);
        }
    }

    private int generateMinDay(final int year, final int month) {
        final boolean biggerThanCurrentYear = year > this.localDate.getYear();
        final boolean sameYearBiggerCurrentMonth = year == this.localDate.getYear() && month > this.localDate.getMonthValue();
        if (biggerThanCurrentYear || sameYearBiggerCurrentMonth) {
            return 1;
        }
        return this.localDate.getDayOfMonth();
    }

    private Date askDate() {
        final int year = this.askYear();

        final int month = this.askMonth(year);

        final int minDay = this.generateMinDay(year, month);
        final int maxDay = YearMonth.of(year, month).lengthOfMonth();
        final int day = this.askDay(minDay, maxDay);

        return valueOf(LocalDate.of(year, month, day));
    }

    private Item askItemID() {
        while (true) {
            System.out.print("Please enter the ID of the ticket you wish to order: ");
            final String itemID = this.scanner.nextLine();
            try {
                final Item item = this.controller.searchItemByItemID(itemID);
                if (item != null) {
                    return item;
                }
                System.out.println("Please enter valid ID");
            } catch (final NumberFormatException e) {
                System.out.println("Please enter valid ID");
            }
        }
    }

    private int askQuantity() {
        while (true) {
            System.out.print("Please enter the quantity of the item you wish to order: ");
            final String quantity = this.scanner.nextLine();
            if (NumericValidation.tryParseIntInRange(quantity, 1, Integer.MAX_VALUE)) {
                return Integer.parseInt(quantity);
            }
            System.out.println("Please enter positive integer only");
        }
    }

    private String askClientContinueBookingOption() {
        System.out.print("Would you like to continue booking? (Press Enter - Continue Booking || 2 - Confirm Booking || 3 - Cancel Booking Now and Back): ");
        return this.scanner.nextLine();
    }


    //view All booking
    private void adminViewBookingDetails() {
        if (this.controller.allBookingRecordSize() == 0) {
            System.out.println("You do not have any booking records");
        } else {
            this.adminBookingOptions();
        }
    }

    private void clientViewBookingDetails(final int clientID) {
        if (this.controller.bookingRecordSizeForSpecifiedClient(clientID) == 0) {
            System.out.println("You do not have any booking records");
        } else {
            this.clientBookingOptions(clientID);
        }
    }

    private void adminBookingOptions() {
        this.showFilteredBooking(this.controller.getBookingList());
        final Booking booking = this.askAdminBookingID();
        System.out.println(this.adminBookingDetails(booking));
        this.askBookingOption(booking);
    }

    private void clientBookingOptions(final int clientID) {
        this.showFilteredBookingOfAClient(clientID, this.controller.getBookingList());
        final Booking booking = this.askClientBookingID(clientID);
        System.out.println(this.clientBookingDetails(booking));
        this.askBookingOption(booking);
    }

    private Booking askAdminBookingID() {
        while (true) {
            System.out.print("Please enter the Booking ID: ");
            final String bookingID = this.scanner.nextLine();
            try {
                final Booking booking = this.controller.getBookingByBookingID(Integer.parseInt(bookingID));
                if (booking != null) {
                    return booking;
                }
                System.out.println("Please enter valid Booking ID");
            } catch (final NumberFormatException e) {
                System.out.println("Please enter valid Booking ID(Positive Integer ONLY)");
            }
        }
    }

    private Booking askClientBookingID(final int clientID) {
        while (true) {
            System.out.print("Please enter the Booking ID: ");
            final String bookingID = this.scanner.nextLine();
            try {
                final Booking booking = this.controller.getBookingByBookingAndClientID(bookingID, clientID);
                if (booking != null) {
                    return booking;
                }
                System.out.println("Please enter valid Booking ID");
            } catch (final NumberFormatException e) {
                System.out.println("Please enter valid Booking ID(Positive Integer ONLY)");
            }
        }
    }


    //search booking
    //client search booking
    private void clientSearchEngine(final int clientID) {
        while (true) {
            System.out.print("Search (Booking ID / Ticket Date(yyyy-mm-dd, Eg. 2021-3-18) / (X/x to Exit)) ): ");
            final String searchData = this.scanner.nextLine();
            if ("x".equalsIgnoreCase(searchData)) {
                return;
            }
            if (NumericValidation.tryParseIntInRange(searchData, 0, Integer.MAX_VALUE)) {
                final Booking booking = this.controller.getBookingByBookingAndClientID(searchData, clientID);
                if (booking != null) {
                    System.out.println(this.clientBookingDetails(booking));
                    this.askBookingOption(booking);
                    return;
                }
                System.out.println("Please enter valid Booking ID!");

            } else {
                final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                try {
                    final Booking booking = this.clientSearchBookingByTicketDate(dateFormat.parse(searchData), clientID);
                    if (booking != null) {
                        System.out.println(this.clientBookingDetails(booking));
                        this.askBookingOption(booking);
                        return;
                    }
                    System.out.println("The date you've entered is not in our record");

                } catch (final ParseException e) {
                    System.out.println("Please enter specified date format (yyyy-mm-dd, Eg. 2021-3-18)");
                }
            }
        }
    }

    private void adminSearchEngine() {
        while (true) {
            System.out.print("Search (Client ID / Booking ID / Ticket Date(yyyy-mm-dd, Eg. 2021-3-18) / (X/x to Exit)) ): ");
            final String searchData = this.scanner.nextLine();
            if ("x".equalsIgnoreCase(searchData)) {
                return;
            }
            if (NumericValidation.tryParseIntInRange(searchData, 0, Integer.MAX_VALUE)) {
                final int unknownID = Integer.parseInt(searchData);
                final List<Booking> bookingList = this.controller.searchBookingRecordByClientID(unknownID);
                if (bookingList.isEmpty()) {
                    final Booking booking = this.controller.getBookingByBookingID(unknownID);
                    if (booking == null) {
                        System.out.println("Please enter valid value");
                    } else {
                        System.out.println(this.adminBookingDetails(booking));
                        this.askBookingOption(booking);
                        return;
                    }
                } else {
                    this.showFilteredBookingOfAClient(unknownID, bookingList);
                    final Booking booking = this.askAdminBookingID();
                    System.out.println(this.adminBookingDetails(booking));
                    this.askBookingOption(booking);
                    return;
                }
            } else {
                final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                try {
                    final Booking booking = this.adminSearchBookingByTicketDate(dateFormat.parse(searchData));
                    if (booking != null) {
                        System.out.println(this.adminBookingDetails(booking));
                        this.askBookingOption(booking);
                        return;
                    }
                    System.out.println("The date you've entered is not in our record");

                } catch (final ParseException e) {
                    System.out.println("Please enter specified date format (yyyy-mm-dd, Eg. 2021-3-18)");
                }
            }
        }
    }

    private Booking clientSearchBookingByTicketDate(final Date date, final int clientID) {
        final List<Booking> allBookingsSameDate = this.controller.clientGetBookingRecordByTicketDateAndClientID(date, clientID);
        if (!allBookingsSameDate.isEmpty()) {
            this.showFilteredBookingOfAClient(clientID, allBookingsSameDate);
            return this.askClientBookingID(clientID);
        }
        return null;
    }

    private Booking adminSearchBookingByTicketDate(final Date date) {
        final List<Booking> allBookingsSameDate = this.controller.adminGetBookingRecordByTicketDate(date);
        if (!allBookingsSameDate.isEmpty()) {
            this.showFilteredBooking(allBookingsSameDate);
            return this.askAdminBookingID();
        }
        return null;
    }

    private void updateBooking(final Booking oldBooking) {
        System.out.println("\nNow, enter the new ticket date as below");
        final Booking newBooking = new Booking(oldBooking, this.askDate());
        this.controller.updateBooking(newBooking, oldBooking);
    }

    private void cancelBooking(final Booking booking) {
        System.out.println("Please enter (Y/y to confirm cancel || Press Enter to deny)");
        if ("y".equalsIgnoreCase(this.scanner.nextLine())) {
            this.controller.deleteBooking(booking);
        }
    }



    //add User
    private User createNewClient() {
        final int clientID = this.controller.generateNewClientID();
        return new User(clientID, this.askName(), this.askContact(), 'C', this.askPassword());
    }

    private String askName() {
        while (true) {
            System.out.print("Please enter your name: ");
            final String name = this.scanner.nextLine();
            if (name.isEmpty() || name.isBlank()) {
                System.out.println("Name cannot be empty");
            }
            else {
                return name;
            }
        }
    }


    private String askContact() {
        while (true) {
            System.out.print("Please enter your phone number (+601): ");
            final String contact = this.scanner.nextLine();
            if (this.isContactCorrect(contact)) {

                final String properContact = "01" + contact;

                if (this.controller.searchNoSameContact(properContact)) {
                    return properContact;
                }

                System.out.println("The contact number you've entered is already in the system");

            } else {
                System.out.println("Please enter proper format of contact number");
            }
        }
    }

    private boolean isContactCorrect(final String contact) {
        if (!contact.isEmpty() || !contact.isBlank()) {
            final boolean isElevenDigit = contact.charAt(0) == '1' && NumericValidation.tryParseIntInRange(contact, 100000000, 199999999);
            final boolean isTenDigit;
            if (contact.charAt(0) == '0') {
                isTenDigit = NumericValidation.tryParseIntInRange(contact, 1000000, 9999999);
            } else {
                isTenDigit = NumericValidation.tryParseIntInRange(contact, 20000000, 99999999);
            }
            return isElevenDigit || isTenDigit;
        }
        return false;
    }

    private String askPassword() {
        while (true) {
            System.out.print("Please enter your password: ");
            final String password = this.scanner.nextLine();
            if (password.isEmpty() || password.isBlank()) {
                System.out.println("Password cannot be empty/blank");
            }
            else {
                System.out.print("Please confirm your password: ");
                final String confirmPassword = this.scanner.nextLine();
                if (password.equals(confirmPassword)) {
                    return password;
                } else {
                    System.out.println("Password do not match. Please try again");
                }
            }
        }
    }


    //output
    //display only

    private void showItemHeader() {
        System.out.println("-".repeat(154));
        System.out.printf("%-20s%-60s%-60s%-5s%n", "Item ID", "Item Name", "Item Description", "Item Price(RM)");
        System.out.println("-".repeat(154));
    }

    private void showItems() {
        this.showItemHeader();
        this.controller.getItemRecord().viewData();
    }

    //show full details of a booking record for a client
    private String clientBookingDetails(final Booking booking) {
        return this.bookingSummaryForClient(booking) + "\n" + allItemsOrdered(booking.getItemOrdered());
    }

    //show full details of a booking record
    private String adminBookingDetails(final Booking booking) {
        return this.bookingSummaryForAdmin(booking) + "\n" + allItemsOrdered(booking.getItemOrdered());
    }

    //show general information of a booking record
    private String bookingBasicInfo(final Booking booking) {
        return String.format("%-20d%-14s%-14s%-7.2f", booking.getBookID(), booking.getBookDate().toString(), booking.getTicketDate().toString(), booking.getTotalPrice());
    }



    private String bookingSummaryForClient(final Booking booking) {
        return "\nBooking ID     : " + booking.getBookID() +
                "\nBook Date      : " + booking.getBookDate().toString() +
                "\nTicket Date    : " + booking.getTicketDate().toString() +
                "\nTotal Price(RM): " + String.format("%.2f", booking.getTotalPrice());
    }

    private String bookingSummaryForAdmin(final Booking booking) {
        return "\nUser ID        : " + booking.getClientID() +
                "\nBooking ID     : " + booking.getBookID() +
                "\nBook Date      : " + booking.getBookDate().toString() +
                "\nTicket Date    : " + booking.getTicketDate().toString() +
                "\nTotal Price(RM): " + String.format("%.2f", booking.getTotalPrice());
    }

    private String allItemsOrdered(final Map<Item, Integer> itemOrdered) { return bookingHeader() + "\n" + organisedItemsOrderedInfo(itemOrdered) + "\n" + totalPriceOfOrders(itemOrdered); }


    //show header for details of booking
    private String bookingHeader() {
        return "-".repeat(200)
                + "\n"
                + String.format("%-20s%-36s%-50s%-20s%-20s%-20s", "Item ID", "Item Name", "Item Description", "Item Price(RM)", "Quantity Ordered", "Final Price(RM)")
                + "\n"
                + "-".repeat(200);
    }

    private String organisedItemsOrderedInfo(final Map<Item, Integer> itemOrdered) {
        final StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        final int size = itemOrdered.size();
        for (final Map.Entry<Item, Integer> entryItem : itemOrdered.entrySet()) {
            final Item item = entryItem.getKey();
            final int quantity = entryItem.getValue();
            i++;
            final String details = item.itemDetailsPrettyPrint() +
                    String.format("%-20s", quantity) +
                    String.format("%-20.2f", item.getItemPrice() * quantity);
            if (i < size) {
                stringBuilder.append(details).append("\n");
            } else {
                stringBuilder.append(details);
            }
        }
        return stringBuilder.toString();
    }

    private String totalPriceOfOrders(final Map<Item, Integer> itemOrdered) {
        double price = 0;
        for (final Map.Entry<Item, Integer> entryItem : itemOrdered.entrySet()) {
            final int quantity = entryItem.getValue();
            price += entryItem.getKey().getItemPrice() * quantity;
        }
        return "Total price(RM): " + String.format("%.2f", price);
    }


    private void showFilteredBooking(final List<Booking> bookingList) {
        System.out.println("-".repeat(100));
        System.out.printf("%-20s%-20s%-14s%-14s%-8s", "Client ID","Booking ID", "Booking Date", "Ticket Date", "Total Price(RM)");
        System.out.println();
        System.out.println("-".repeat(100));
        bookingList.forEach(booking -> System.out.println(String.format("%-20s", booking.getClientID()) + this.bookingBasicInfo(booking)));
    }


    private void showFilteredBookingOfAClient(final int clientID, final List<Booking> bookingList) {
        System.out.println("-".repeat(64));
        System.out.printf("%-20s%-14s%-14s%-8s", "Booking ID", "Booking Date", "Ticket Date", "Total Price(RM)");
        System.out.println();
        System.out.println("-".repeat(64));
        bookingList
                .stream()
                .filter(booking -> booking.getClientID() == clientID)
                .collect(Collectors.toUnmodifiableList())
                .forEach(booking -> System.out.println(this.bookingBasicInfo(booking)));
    }
}