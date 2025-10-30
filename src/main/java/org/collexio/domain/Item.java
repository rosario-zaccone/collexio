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

public abstract class Item {
    private final int id; //I-001
    private String name;
    private int quantity;
    private ItemPhoto photo;
    private String description;

    private final Set<Transaction> transactions = new TreeSet<>();


    public Item(int id, String name, int quantity, ItemPhoto photo) {
        if (id <= 0)
            throw new IllegalArgumentException("Id must be positive");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        if (name.isEmpty() || !name.matches("[\\p{L}\\p{N} ]+"))
            throw new IllegalArgumentException("Name can't be empty and can contain only letters, number and spaces");
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.photo = photo;
        this.description = "NO DESCRIPTION";
    }

    public Item(String name, int quantity, ItemPhoto photo) {
        this(1, name, quantity, photo);
    }



    public int getId() {
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

    public abstract Item copy() throws IOException;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item item)) return false;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", photo=" + photo +
                ", transactions=" + transactions +
                '}';
    }
}
