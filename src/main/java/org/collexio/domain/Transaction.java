package org.collexio.domain;

import org.collexio.utilities.Utilities;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction implements Comparable<Transaction> {
    private final Long id;
    private final double amount;
    private final boolean income;
    private final LocalDate date;

    public Transaction(Long id, double amount, boolean income, LocalDate date) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("Id must be positive");
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be non negative");
        this.id = id;
        this.amount = amount;
        this.income = income;
        this.date = date;
    }

    public Transaction(double amount, boolean income, LocalDate date) {
        this(null, amount, income, date);
    }



    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isIncome() {
        return income;
    }

    public LocalDate getDate() {
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

    public String toStringNoId() {
        return "Transaction{" +
                "amount=" + amount +
                ", income=" + income +
                ", date=" + date +
                '}';
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

    @Override
    public int compareTo(@NotNull Transaction o) {
        int cmp = date.compareTo(o.date);
        if (cmp != 0) return cmp;
        return id == null || o.id == null ? 0 : id.compareTo(o.id);
    }
}
