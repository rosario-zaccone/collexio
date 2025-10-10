package org.collexio.domain;


import org.collexio.utilities.Utilities;

import java.util.Objects;

public abstract class Item {
    private final String id; //I-001
    private String name;
    private int quantity;

    public Item(String id, String name, int quantity) {
        if (!Utilities.validateId("I-", id))
            throw new IllegalArgumentException("Id must be in the format I-###, where ### is a natural number");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        if (name.isEmpty())
            throw new IllegalArgumentException("Name can't be empty");
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        if (name.isEmpty())
            throw new IllegalArgumentException("Name can't be empty");
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
