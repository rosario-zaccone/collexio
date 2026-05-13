package org.collexio.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTypeTest {

    @Test
    void mapsIntegerValues() {
        assertEquals(ItemType.PLANT, ItemType.fromInt(0));
        assertEquals(ItemType.TECHITEM, ItemType.fromInt(1));
        assertEquals(ItemType.BOOK, ItemType.fromInt(2));
        assertThrows(IllegalArgumentException.class, () -> ItemType.fromInt(3));
    }

    @Test
    void mapsStringValues() {
        assertEquals(ItemType.PLANT, ItemType.fromString("Plant"));
        assertEquals(ItemType.TECHITEM, ItemType.fromString("Tech Item"));
        assertEquals(ItemType.BOOK, ItemType.fromString("Book"));
        assertThrows(IllegalArgumentException.class, () -> ItemType.fromString("Other"));
    }

    @Test
    void exposesValuesAndLabels() {
        assertEquals(1, ItemType.TECHITEM.getValue());
        assertEquals("Book", ItemType.BOOK.toString());
    }
}
