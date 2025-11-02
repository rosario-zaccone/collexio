package org.collexio.domain;


import org.collexio.utilities.Utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class Item {
    private final Long id; //I-001
    private String name;
    private int quantity;
    private ItemPhoto photo;
    private String description;

    private final Set<Transaction> transactions = new TreeSet<>();


    public Item(Long id, String name, int quantity, ItemPhoto photo, String description) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("Id must be positive or null");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        if (name.isEmpty())
            throw new IllegalArgumentException("Name can't be empty");
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.photo = photo;
        this.description = description;
    }

    public Item(Long id, String name, int quantity, ItemPhoto photo) {
        this(id, name, quantity, photo, "no description");
    }

    public Item(String name, int quantity, ItemPhoto photo, String description) {
        this(null, name, quantity, photo, description);
    }



    public Long getId() {
        return id;
    }

    public void setName(String name) {
        if (name.isEmpty() || !name.matches("[\\p{L}\\p{N} ]+"))
            throw new IllegalArgumentException("Name can't be empty and can contain only letters, number and spaces");
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract void generateDescription();

    public void setPhoto(ItemPhoto photo) {
        this.photo = photo;
    }

    public ItemPhoto getPhoto() {
        return photo;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Set<Transaction> getTransactions() {
        return Collections.unmodifiableSet(transactions);
    }

    public abstract Item copy();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return quantity == item.quantity && Objects.equals(name, item.name) && Objects.equals(photo, item.photo) && Objects.equals(description, item.description) && Objects.equals(transactions, item.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, photo, description, transactions);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", photo=" + photo +
                ", description=" + description +
                ", transactions=" + transactions +
                '}';
    }

    public String toStringNoId() {
        return "Item{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", photo=" + photo.toStringNoId() +
                ", description=" + description +
                ", transactions=" +             transactions.stream()
                .map(Transaction::toStringNoId)
                .collect(Collectors.joining(", ")) + +
                '}';
    }
}
