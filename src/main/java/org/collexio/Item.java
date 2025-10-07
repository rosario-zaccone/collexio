package org.collexio;


import java.util.Objects;

public abstract class Item {
    private final String id; //I-001
    private String name;
    private int quantity;

    public Item(String id, String name, int quantity) {
        if (!validateId(id))
            throw new IllegalArgumentException("Id must be in the format I-###, where ### is a number (001, 010, 124...)");

        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    private boolean validateId(String value) {
        if (value.length() != 5 || !value.substring(0, 2).equals("I-") )
            return false;
        String suffix = value.substring(2, 5);
        for (Character c: suffix.toCharArray())
            if (!Character.isDigit(c))
                return false;
        return true;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public abstract String description();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item item)) return false;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
