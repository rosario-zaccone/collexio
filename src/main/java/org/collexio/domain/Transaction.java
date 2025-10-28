package org.collexio.domain;

import org.collexio.utilities.Utilities;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction implements Comparable<Transaction> {
    private final int id;
    private final double amount;
    private final boolean income;
    private final LocalDateTime date;

    public Transaction(int id, double amount, boolean income, LocalDateTime date) {
        if (id <= 0)
            throw new IllegalArgumentException("Id must be positive");
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be non negative");
        this.id = id;
        this.amount = amount;
        this.income = income;
        this.date = date;
    }

    public Transaction(double amount, boolean income, LocalDateTime date) {
        this(1, amount, income, date);
    }



    public int getId() {
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
