package backend.interfaces;

public interface IBookingFunction<T> extends IDataFunction<T> {
    void viewData();
    void updateData(T newObject, T oldObject);
    void deleteData(T t);
}