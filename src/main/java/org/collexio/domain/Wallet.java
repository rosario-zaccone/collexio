package org.collexio.domain;

import java.util.TreeSet;

public class Wallet {
    private double amount = 0.0;
    private final TreeSet<Transaction> transactions;

    public Wallet (TreeSet<Transaction> transactions) {
        this.transactions = transactions;
        for (Transaction t: transactions) {
            if (t.isIncome())
                amount += t.getPrice();
            else
                amount -= t.getPrice();
        }
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        if (transaction.isIncome())
            amount += transaction.getPrice();
        else
            amount -= transaction.getPrice();
    }

    public boolean removeTransaction(Transaction transaction) {
        boolean removed = transactions.remove(transaction);
        if (removed) {
            if (transaction.isIncome())
                amount -= transaction.getPrice();
            else
                amount += transaction.getPrice();
        }
        return removed;
    }

    public double getAmount() {
        return amount;
    }
}
