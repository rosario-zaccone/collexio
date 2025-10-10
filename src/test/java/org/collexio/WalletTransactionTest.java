package org.collexio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WalletTransactionTest {

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        TreeSet<Transaction> initialTransactions = new TreeSet<>();
        initialTransactions.add(new Transaction("T-1", 100, true, "I-1", LocalDate.of(2023, 5, 1)));
        initialTransactions.add(new Transaction("T-2", 50, false, "I-2", LocalDate.of(2023, 5, 3)));
        initialTransactions.add(new Transaction("T-3", 75, true, "I-3", LocalDate.of(2023, 5, 2)));
        initialTransactions.add(new Transaction("T-4", 20, false, "I-4", LocalDate.of(2023, 5, 4)));
        initialTransactions.add(new Transaction("T-5", 150, true, "I-5", LocalDate.of(2023, 5, 5)));

        wallet = new Wallet(initialTransactions);
    }

    @Test
    void testInitialAmount() {
        assertEquals(255, wallet.getAmount(), 0.001);
    }

    @Test
    void testAddRemoveTransaction() {
        Transaction newTransaction = new Transaction("T-6", 30, true, "I-6", LocalDate.of(2023, 5, 6));
        wallet.addTransaction(newTransaction);
        assertEquals(285, wallet.getAmount(), 0.001);
        Transaction toRemove = new Transaction("T-3", 75, true, "I-3", LocalDate.of(2023, 5, 2));
        boolean removed = wallet.removeTransaction(toRemove);
        assertTrue(removed);
        assertEquals(210, wallet.getAmount(), 0.001);
    }

}
