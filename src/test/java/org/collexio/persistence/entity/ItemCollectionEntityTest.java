package org.collexio.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemCollectionEntityTest {

    @Test
    void storesValuesAndItems() {
        ItemCollectionEntity collection = new ItemCollectionEntity(1L, "Console");
        ItemEntity item = new ItemEntity(2L, ItemStatus.GOOD);

        collection.addItem(item);

        assertEquals(1L, collection.getId());
        assertEquals("Console", collection.getName());
        assertEquals(1, collection.getData().size());
    }

    @Test
    void returnsUnmodifiableData() {
        ItemCollectionEntity collection = new ItemCollectionEntity("Console");

        assertThrows(UnsupportedOperationException.class,
                () -> collection.getData().add(new ItemEntity(1L, ItemStatus.GOOD)));
    }

    @Test
    void removesItemsAndEqualityUsesNameAndData() {
        ItemEntity item = new ItemEntity(1L, ItemStatus.GOOD);
        ItemCollectionEntity first = new ItemCollectionEntity(1L, "Console");
        ItemCollectionEntity second = new ItemCollectionEntity(2L, "Console");
        first.addItem(item);
        second.addItem(item);

        assertEquals(first, second);

        first.removeItem(item);

        assertNotEquals(first, second);
    }
}
