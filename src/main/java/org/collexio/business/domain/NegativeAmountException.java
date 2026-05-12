package org.collexio.business.domain;

public class NegativeAmountException extends IllegalArgumentException {
    public NegativeAmountException(double amount) {
        super("A transaction can't have a negative amount: " + amount);
    }
}
