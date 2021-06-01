package backend.interfaces;

import java.util.List;

public interface IDataFunction<T> {
    void addData(T t);
    int generateNewID();
    List<T> readElementFromFile();
    void writeElementToFile();
    List<T> getIDataList();
    int size();
    T getData(Object... object);
}