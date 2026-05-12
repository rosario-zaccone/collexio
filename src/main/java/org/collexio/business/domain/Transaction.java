package org.collexio.business.domain;

import org.collexio.persistence.entity.TransactionEntity;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Objects;

// Transaction represents a transaction involving a single item.
// For example: buying a Nintendo DS or selling a Nintendo DS.
// Important: if you buy two identical items, you must create two separate transactions.
// The same applies to selling.
public class Transaction implements Comparable<Transaction> {
    private final Long id;
    private final double amount;
    private final boolean income;
    private final LocalDate date;

    public Transaction(Long id, double amount, boolean income, LocalDate date) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("Id must be positive");
        if (amount < 0)
            throw new NegativeAmountException(amount);
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

    public static Transaction fromEntity(TransactionEntity entity) {
        return new Transaction(entity.getId(), entity.getAmount(), entity.isIncome(), entity.getDate());
    }

    public TransactionEntity toEntity() {
        return new TransactionEntity(this.id, this.amount, this.income, this.date);
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

