package backend;

import backend.booking.Booking;
import backend.booking.BookingList;
import backend.interfaces.IBookingFunction;
import backend.interfaces.IBookingListFunction;
import backend.interfaces.IDataFunction;
import backend.item.Item;
import backend.item.ItemList;
import backend.user.User;
import backend.user.UserList;

import java.util.Date;
import java.util.List;

public final class Controller {

    private final IBookingFunction<Item> itemRecord;
    private final IBookingListFunction<Booking> bookingRecord;
    private final IDataFunction<User> userRecord;

    public Controller() {
        this.itemRecord = new ItemList();
        this.userRecord = new UserList();
        this.bookingRecord = new BookingList();
    }

    public IBookingFunction<Item> getItemRecord() { return this.itemRecord; }

    public List<Booking> getBookingList() { return this.bookingRecord.getIDataList(); }

    public void addClient(final User user) { this.userRecord.addData(user); }
    public void addBooking(final Booking booking) { this.bookingRecord.addData(booking); }
    public void addItem(final Item item) { this.itemRecord.addData(item); }

    public void updateBooking(final Booking newBooking, final Booking oldBooking) { this.bookingRecord.updateData(newBooking, oldBooking); }
    public void updateItem(final Item newItem, final Item oldItem) { this.itemRecord.updateData(newItem, oldItem); }

    public void deleteBooking(final Booking booking) { this.bookingRecord.deleteData(booking); }
    public void deleteItem(final Item item) { this.itemRecord.deleteData(item); }

    public List<Booking> clientGetBookingRecordByTicketDateAndClientID(final Date date, final int userID) { return this.bookingRecord.searchBookingListByTicketDateAndClientID(date, userID); }
    public List<Booking> adminGetBookingRecordByTicketDate(final Date date) { return this.bookingRecord.searchBookingListByTicketDate(date); }

    public Item searchItemByItemID(final String itemID) { return this.itemRecord.getData(itemID); }
    public User searchUserByIDPassword(final String ID, final String password) { return this.userRecord.getData(ID, password); }
    public Booking getBookingByBookingID(final int bookID) { return this.bookingRecord.getData(bookID); }
    public Booking getBookingByBookingAndClientID(final String bookID, final int userID) { return this.bookingRecord.getData(Integer.parseInt(bookID), userID); }
    public List<Booking> searchBookingRecordByClientID(final int userID) { return this.bookingRecord.searchBookingByClientID(userID); }

    public int allBookingRecordSize() { return this.bookingRecord.size(); }
    public int bookingRecordSizeForSpecifiedClient(final int userID) { return this.bookingRecord.numberOfBookingRecordOfAClient(userID); }

    public boolean searchSameItemName(final String name) { return this.itemRecord.getIDataList().stream().anyMatch(item -> item.getItemName().equals(name)); }
    public boolean searchSameItemDescription(final String description) { return this.itemRecord.getIDataList().stream().anyMatch(item -> item.getDescription().equals(description)); }

    public int generateNewClientID() { return this.userRecord.generateNewID(); }
    public int generateNewItemID() { return this.itemRecord.generateNewID(); }
    public int generateNewBookingID() { return this.bookingRecord.generateNewID(); }

    public boolean searchNoSameContact(final String contact) { return this.userRecord.getIDataList().stream().noneMatch(user -> user.getPhoneNum().equals(contact)); }
}