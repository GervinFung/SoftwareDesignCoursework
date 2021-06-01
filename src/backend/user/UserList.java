package backend.user;

import backend.interfaces.IDataFunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class UserList implements IDataFunction<User> {

    private static final String USER_TXT = "textFiles/user.txt";

    private final List<User> userList;

    public UserList() { this.userList = this.readElementFromFile(); }

    @Override
    public List<User> readElementFromFile() {

        final List<User> userList = new ArrayList<>();

        try (final BufferedReader reader = new BufferedReader(new FileReader(USER_TXT))) {

            String line;
            while ((line = reader.readLine()) != null) {
                final String[] data = line.split("/");
                final int ID = Integer.parseInt(data[0]);
                final String name = data[1];
                final String phoneNum = data[2];
                final char c = data[3].charAt(0);
                final String password = data[4];
                userList.add(new User(ID, name, phoneNum, c, password));
            }

            return userList;

        } catch (final IOException e) { throw new RuntimeException("Error reading " + USER_TXT); }
    }

    @Override
    public void writeElementToFile() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(USER_TXT))) {

            for (final User user : this.userList) {
                writer.write(user.toString());
                writer.newLine();
            }

        } catch (final IOException e) { throw new RuntimeException("Error writing " + USER_TXT); }
    }

    @Override
    public void addData(final User user) {
        this.userList.add(user);
        this.writeElementToFile();
    }

    @Override
    public int generateNewID() {
        final User userWithLargestID = this.userList
                .stream()
                .max(Comparator.comparing(User::getUserID))
                .orElse(null);
        return userWithLargestID == null ? 1800003 : userWithLargestID.getUserID() + 1;
    }

    @Override
    public List<User> getIDataList() { return this.userList; }

    @Override
    public int size() { return this.userList.size(); }

    @Override
    public User getData(final Object... object) {
        if (object.length != 2) {
            throw new IllegalArgumentException("Only 2(user ID and password) arguments");
        }
        if (!(object[0] instanceof String) || !(object[1] instanceof String)) {
            throw new IllegalArgumentException("Only accept String argument");
        }
        final int userID = Integer.parseInt((String)object[0]);
        final String password = (String)object[1];
        return this.userList
                .stream()
                .filter(user -> (user.getUserID() == userID && user.getPassword().equals(password)))
                .findAny()
                .orElse(null);
    }
}