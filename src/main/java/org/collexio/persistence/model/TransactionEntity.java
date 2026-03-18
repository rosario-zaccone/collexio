package org.collexio.persistence.model;
import java.time.LocalDate;
import java.util.Objects;

public class TransactionEntity implements Comparable<TransactionEntity> {
    private final Long id;
    private final double amount;
    private final boolean income;
    private final LocalDate date;

    public TransactionEntity(Long id, double amount, boolean income, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.income = income;
        this.date = date;
    }

    public TransactionEntity(double amount, boolean income, LocalDate date) {
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
        TransactionEntity that = (TransactionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public int compareTo(TransactionEntity o) {
        int cmp = date.compareTo(o.date);
        if (cmp != 0) return cmp;
        return id == null || o.id == null ? 0 : id.compareTo(o.id);
    }
}