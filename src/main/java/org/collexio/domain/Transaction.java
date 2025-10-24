package org.collexio.domain;

import org.collexio.utilities.Utilities;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction implements Comparable<Transaction> {
    private String id;
    private double amount;
    private boolean income;
    private LocalDateTime date;

    public Transaction(String id, double amount, boolean income, LocalDateTime date) {
        if (!Utilities.validateId("T-", id))
            throw new IllegalArgumentException("Id must be in the format T-###, where ### is a natural number");
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be non negative");
        this.id = id;
        this.amount = amount;
        this.income = income;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isIncome() {
        return income;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", income=" + income +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public int compareTo(@NotNull Transaction o) {
        return date.compareTo(o.date);
    }
}
