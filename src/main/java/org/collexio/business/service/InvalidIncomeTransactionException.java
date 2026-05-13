package org.collexio.business.service;

public class InvalidIncomeTransactionException extends IllegalArgumentException {
    public InvalidIncomeTransactionException() {
        super("The last transaction is not an expense, you can't enter an income transaction");
    }
}
