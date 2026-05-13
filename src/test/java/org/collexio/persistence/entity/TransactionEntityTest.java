package org.collexio.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionEntityTest {

    @Test
    void storesValues() {
        TransactionEntity transaction = new TransactionEntity(1L, 12.5, true, LocalDate.of(2024, 1, 1));

        assertEquals(1L, transaction.getId());
        assertEquals(12.5, transaction.getAmount());
        assertTrue(transaction.isIncome());
        assertEquals(LocalDate.of(2024, 1, 1), transaction.getDate());
    }

    @Test
    void comparesByDateThenId() {
        TransactionEntity first = new TransactionEntity(1L, 10, true, LocalDate.of(2024, 1, 1));
        TransactionEntity second = new TransactionEntity(2L, 10, true, LocalDate.of(2024, 1, 1));
        TransactionEntity third = new TransactionEntity(3L, 10, true, LocalDate.of(2024, 1, 2));

        assertTrue(first.compareTo(second) < 0);
        assertTrue(third.compareTo(second) > 0);
    }

    @Test
    void equalityUsesId() {
        assertEquals(
                new TransactionEntity(1L, 10, true, LocalDate.now()),
                new TransactionEntity(1L, 20, false, LocalDate.now().plusDays(1))
        );
    }
}
