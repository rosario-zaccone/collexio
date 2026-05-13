package org.collexio.persistence.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemEntityTest {

    @Test
    void storesConstructorValues() {
        ItemSpecEntity spec = new ItemSpecEntity(1L, ItemType.TECHITEM, "DS", "Desc");
        ItemEntity item = new ItemEntity(2L, ItemStatus.GOOD, spec);

        assertEquals(2L, item.getId());
        assertEquals(ItemStatus.GOOD, item.getStatus());
        assertEquals(spec, item.getSpec());
        assertNull(item.getPhoto());
    }

    @Test
    void managesTransactions() {
        ItemEntity item = new ItemEntity(1L, ItemStatus.GOOD);
        TransactionEntity transaction = new TransactionEntity(1L, 10, true, java.time.LocalDate.now());

        item.addTransaction(transaction);
        item.removeTransaction(transaction);

        assertTrue(item.getTransactions().isEmpty());
    }

    @Test
    void returnsUnmodifiableTransactions() {
        ItemEntity item = new ItemEntity(1L, ItemStatus.GOOD);

        assertThrows(UnsupportedOperationException.class,
                () -> item.getTransactions().add(new TransactionEntity(10, true, java.time.LocalDate.now())));
    }

    @Test
    void equalityUsesId() {
        assertEquals(new ItemEntity(1L, ItemStatus.BAD), new ItemEntity(1L, ItemStatus.GOOD));
    }
}
