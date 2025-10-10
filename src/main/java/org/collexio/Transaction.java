package org.collexio;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction implements Comparable <Transaction> {
    private final String id;
    private final double price;
    private final boolean income; // è un'entrata o un'uscita?
    private final String itemId;
    private final LocalDate date;

    public Transaction(String id, double price, boolean income, String itemId, LocalDate date) {
        if (!Utilities.validateId("T-", id))
            throw new IllegalArgumentException("Invalid id: the id must be in the format T-X where X is a natural number");
        if (!Utilities.validateId("I-", itemId))
            throw new IllegalArgumentException("Invalid id: the id must be in the format I-X where X is a natural number");
        if (price < 0)
            throw new IllegalArgumentException("Price must be positive or zero");
        this.id = id;
        this.price = price;
        this.income = income;
        this.itemId = itemId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public boolean isIncome() {
        return income;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getItemId() {
        return itemId;
    }

    @Override
    public int compareTo(@NotNull Transaction o) {
        return date.compareTo(o.getDate());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
