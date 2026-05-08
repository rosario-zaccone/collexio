package org.collexio.business.domain;

import org.collexio.persistence.entity.TransactionEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void rejectsInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(0L, 1, true, LocalDate.now()));
        assertThrows(IllegalArgumentException.class, () -> new Transaction(1L, -1, true, LocalDate.now()));
    }

    @Test
    void comparesByDateThenId() {
        Transaction first = new Transaction(1L, 10, true, LocalDate.of(2024, 1, 1));
        Transaction second = new Transaction(2L, 10, true, LocalDate.of(2024, 1, 1));
        Transaction third = new Transaction(3L, 10, true, LocalDate.of(2024, 1, 2));

        assertTrue(first.compareTo(second) < 0);
        assertTrue(third.compareTo(second) > 0);
    }

    @Test
    void equalityUsesId() {
        assertEquals(
                new Transaction(1L, 10, true, LocalDate.now()),
                new Transaction(1L, 20, false, LocalDate.now().plusDays(1))
        );
    }

    @Test
    void convertsToAndFromEntity() {
        TransactionEntity entity = new TransactionEntity(1L, 20, false, LocalDate.of(2024, 1, 1));

        Transaction transaction = Transaction.fromEntity(entity);

        assertEquals(entity.toString(), transaction.toEntity().toString());
    }
}
