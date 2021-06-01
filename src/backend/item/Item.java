package backend.item;

import java.io.Serializable;

public final class Item implements Serializable {

    private final int itemID;
    private final String name, description;
    private final double price;

    public Item(final int itemID, final String name, final String description, final double price) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Item(final Item item, final String name) {
        this(item.itemID, name, item.description, item.price);
    }

    public Item(final Item item, final double price) {
        this(item.itemID, item.name, item.description, price);
    }

    public Item(final Item item, final CharSequence description) {
        this(item.itemID, item.name, description.toString(), item.price);
    }


    @Override
    public int hashCode() { return this.itemID; }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) { return true; }

        if (!(obj instanceof Item)) { return false; }

        return ((Item)obj).itemID == this.itemID;
    }

    public int getItemID() { return this.itemID; }
    public String getDescription() { return this.description; }
    public String getItemName() { return this.name; }
    public double getItemPrice() { return this.price; }

    @Override
    public String toString() {
        return this.itemID + "/" + this.name + "/" + this.description + "/" + this.price;
    }
    public String itemDetailsPrettyPrint() {
        return String.format("%-20d%-60s%-60s%-20.2f", this.itemID, this.name, this.description, this.price);
    }
}