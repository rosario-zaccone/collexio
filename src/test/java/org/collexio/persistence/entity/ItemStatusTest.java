package org.collexio.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemStatusTest {

    @Test
    void mapsIntegerValues() {
        assertEquals(ItemStatus.BAD, ItemStatus.fromInt(0));
        assertEquals(ItemStatus.AVERAGE, ItemStatus.fromInt(1));
        assertEquals(ItemStatus.GOOD, ItemStatus.fromInt(2));
        assertThrows(IllegalArgumentException.class, () -> ItemStatus.fromInt(3));
    }

    @Test
    void exposesValuesAndLabels() {
        assertEquals(0, ItemStatus.BAD.getValue());
        assertEquals("Average", ItemStatus.AVERAGE.toString());
    }
}
