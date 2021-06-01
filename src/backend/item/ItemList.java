package backend.item;

import backend.interfaces.IBookingFunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ItemList implements IBookingFunction<Item> {

    private static final String ITEM_TXT = "textFiles/item.txt";

    private final List<Item> itemList;

    public ItemList() { this.itemList = this.readElementFromFile(); }

    @Override
    public List<Item> readElementFromFile() {

        final List<Item> itemList = new ArrayList<>();

        try (final BufferedReader reader = new BufferedReader(new FileReader(ITEM_TXT))) {

            String line;
            while ((line = reader.readLine()) != null) {
                final String[] data = line.split("/");
                final int ID = Integer.parseInt(data[0]);
                final String name = data[1];
                final String description = data[2];
                final double price = Double.parseDouble(data[3]);
                itemList.add(new Item(ID, name, description, price));
            }

            reader.close();
            return itemList;

        } catch (final IOException e) { throw new RuntimeException("Error reading " + ITEM_TXT); }
    }

    @Override
    public void writeElementToFile() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(ITEM_TXT))) {

            for (final Item item : this.itemList) {
                writer.write(item.toString());
                writer.newLine();
            }

        } catch (final IOException e) { throw new RuntimeException("Error writing " + ITEM_TXT); }
    }

    @Override
    public void addData(final Item item) {
        this.itemList.add(item);
        this.writeElementToFile();
    }

    @Override
    public int generateNewID() {
        final Item itemWithLargestID = this.itemList
                .stream()
                .max(Comparator.comparing(Item::getItemID))
                .orElse(null);
        return itemWithLargestID == null ? 100000000 : itemWithLargestID.getItemID() + 1;
    }

    @Override
    public void viewData() {
        this.itemList.forEach(item -> System.out.println(item.itemDetailsPrettyPrint()));
    }

    @Override
    public void updateData(final Item newItem, final Item oldItem) {
        final int index = this.itemList.indexOf(oldItem);
        this.itemList.set(index, newItem);
        this.writeElementToFile();
    }

    @Override
    public void deleteData(final Item item) {
        this.itemList.remove(item);
        this.writeElementToFile();
    }

    @Override
    public List<Item> getIDataList() { return this.itemList; }

    @Override
    public int size() { return this.itemList.size(); }

    @Override
    public Item getData(final Object... object) {
        if (object.length != 1) {
            throw new IllegalArgumentException("Only accept 1(Item ID) argument");
        }
        if (!(object[0] instanceof String)) {
            throw new IllegalArgumentException("Only accept String argument");
        }
        final int itemID = Integer.parseInt((String)object[0]);
        return this.itemList
                .stream()
                .filter(item -> item.getItemID() == itemID)
                .findAny()
                .orElse(null);
    }
}