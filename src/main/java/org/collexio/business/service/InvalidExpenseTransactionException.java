package org.collexio.business.service;

public class InvalidExpenseTransactionException extends RuntimeException {
    public InvalidExpenseTransactionException() {
        super("The last transaction an expense, you can't enter another expense transaction");
    }
}
