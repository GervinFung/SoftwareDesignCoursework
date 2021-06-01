package backend.booking;

import backend.item.Item;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public final class Booking implements Serializable {

    private final int clientID, bookID;
    private final Map<Item, Integer> itemOrdered;
    private final Date bookDate;
    private final double totalPrice;
    private final Date ticketDate;

    public Booking(final int clientID, final Map<Item, Integer> itemOrdered, final Date bookDate, final Date ticketDate, final int bookID) {
        this.clientID = clientID;
        this.itemOrdered = Collections.unmodifiableMap(itemOrdered);
        this.bookDate = bookDate;
        this.ticketDate = ticketDate;
        this.totalPrice = this.computeTotalPrice();
        this.bookID = bookID;
    }

    public Booking(final Booking booking, final Date ticketDate) {
        this(booking.clientID, booking.itemOrdered, booking.bookDate, ticketDate, booking.bookID);
    }

    private double computeTotalPrice() {
        double totalPrice = 0;
        for (final Map.Entry<Item, Integer> entryItem : this.itemOrdered.entrySet()) {
            totalPrice += entryItem.getKey().getItemPrice() * entryItem.getValue();
        }
        return totalPrice;
    }

    @Override
    public int hashCode() { return this.bookID; }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) { return true; }

        if (!(obj instanceof Booking)) { return false; }

        return ((Booking)obj).bookID == this.bookID;
    }

    @Override
    public String toString() { return this.bookID + "/" + this.bookDate.toString() + "/" + this.ticketDate.toString() + "/" + this.totalPrice; }

    public Map<Item, Integer> getItemOrdered() { return this.itemOrdered; }
    public double getTotalPrice() { return this.totalPrice; }
    public int getBookID() { return this.bookID; }
    public int getClientID() { return this.clientID; }
    public Date getBookDate() { return this.bookDate; }
    public Date getTicketDate() { return this.ticketDate; }
}