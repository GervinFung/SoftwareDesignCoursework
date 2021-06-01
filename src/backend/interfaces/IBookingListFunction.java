package backend.interfaces;

import java.util.Date;
import java.util.List;

public interface IBookingListFunction<T> extends IBookingFunction<T> {
    List<T> searchBookingListByTicketDateAndClientID(Date date, int clientID);
    List<T> searchBookingListByTicketDate(Date date);
    List<T> searchBookingByClientID(int clientID);
    int numberOfBookingRecordOfAClient(int clientID);
}