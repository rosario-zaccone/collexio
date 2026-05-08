package org.collexio.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemSpecEntityTest {

    @Test
    void storesValuesAndDefaultsDescription() {
        ItemSpecEntity spec = new ItemSpecEntity(1L, ItemType.BOOK, "Book");

        assertEquals(1L, spec.getId());
        assertEquals(ItemType.BOOK, spec.getType());
        assertEquals("Book", spec.getName());
        assertEquals("no description", spec.getDescription());
    }

    @Test
    void equalityUsesId() {
        assertEquals(
                new ItemSpecEntity(1L, ItemType.BOOK, "A", "A"),
                new ItemSpecEntity(1L, ItemType.PLANT, "B", "B")
        );
    }
}
