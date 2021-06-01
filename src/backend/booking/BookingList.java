package backend.booking;

import backend.interfaces.IBookingListFunction;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class BookingList implements IBookingListFunction<Booking> {

    private static final String BOOKING_TXT = "textFiles/booking.txt";

    private final List<Booking> bookingList;

    public BookingList() {
        this.bookingList = this.readElementFromFile();
    }

    @Override
    public int numberOfBookingRecordOfAClient(final int clientID) {
        return this.bookingList
                .stream()
                .filter(booking -> booking.getClientID() == clientID)
                .collect(Collectors.toUnmodifiableList())
                .size();
    }

    @Override
    public int generateNewID() {
        final Booking bookingWithLargestID = this.bookingList
                .stream()
                .max(Comparator.comparing(Booking::getBookID))
                .orElse(null);
        return bookingWithLargestID == null ? 20000000 : bookingWithLargestID.getBookID() + 1;
    }

    @Override
    public List<Booking> searchBookingListByTicketDateAndClientID(final Date date, final int clientID) {
        return this.bookingList
                .stream()
                .filter(booking -> booking.getTicketDate().equals(date) && booking.getClientID() == clientID)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Booking> searchBookingListByTicketDate(final Date date) {
        return this.bookingList
                .stream()
                .filter(booking -> booking.getTicketDate().equals(date))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Booking> searchBookingByClientID(final int clientID) {
        return this.bookingList
                .stream()
                .filter(booking -> booking.getClientID() == clientID)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Booking> readElementFromFile() {
        final List<Booking> bookingList = new ArrayList<>();
        try (final ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(BOOKING_TXT))) {

            ((List<?>) objectIn.readObject()).forEach(object -> bookingList.add((Booking)object));

            return bookingList;

        } catch (final IOException | ClassNotFoundException ex) {
            throw new RuntimeException("Error reading " + BOOKING_TXT);
        }
    }

    @Override
    public void writeElementToFile() {
        try (final ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(BOOKING_TXT))) {

            objectOutput.writeObject(this.bookingList);

        } catch (final IOException ex) { throw new RuntimeException("Error reading " + BOOKING_TXT); }
    }

    @Override
    public void viewData() {
        this.bookingList.forEach(booking -> System.out.println(booking.toString()));
    }

    @Override
    public void updateData(final Booking newBooking, final Booking oldBooking) {
        final int index = this.bookingList.indexOf(oldBooking);
        this.bookingList.set(index, newBooking);
        this.writeElementToFile();
    }

    @Override
    public void deleteData(final Booking booking) {
        this.bookingList.remove(booking);
        this.writeElementToFile();
    }

    @Override
    public void addData(final Booking booking) {
        this.bookingList.add(booking);
        this.writeElementToFile();
    }

    @Override
    public List<Booking> getIDataList() { return this.bookingList; }

    @Override
    public int size() { return this.bookingList.size(); }

    @Override
    public Booking getData(final Object... object) {
        if (object.length == 1) {
            if (!(object[0] instanceof Integer)) {
                throw new IllegalArgumentException("Only integer input(booking ID)");
            }
            final int bookingID = (Integer)object[0];
            return this.bookingList.stream().filter(booking -> booking.getBookID() == bookingID).findAny().orElse(null);
        } else if (object.length == 2) {
            if (!(object[0] instanceof Integer) || !(object[1] instanceof Integer)) {
                throw new IllegalArgumentException("Only integer input(booking ID and user ID)");
            }
            final int bookingID = (Integer)object[0];
            final int userID = (Integer)object[1];
            return this.bookingList.stream().filter(booking -> booking.getBookID() == bookingID && booking.getClientID() == userID).findAny().orElse(null);
        } else {
            throw new IllegalArgumentException("Only 1(booking ID) or 2(booking ID and user ID) arguments");
        }
    }
}