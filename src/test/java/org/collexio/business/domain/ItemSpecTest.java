package org.collexio.business.domain;

import org.collexio.persistence.entity.ItemSpecEntity;
import org.collexio.persistence.entity.ItemType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemSpecTest {

    @Test
    void rejectsInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> new ItemSpec(0L, ItemType.BOOK, "Book", "Desc"));
        assertThrows(IllegalArgumentException.class, () -> new ItemSpec(ItemType.BOOK, "", "Desc"));
    }

    @Test
    void defaultsDescription() {
        ItemSpec spec = new ItemSpec(1L, ItemType.BOOK, "Book");

        assertEquals("no description", spec.getDescription());
    }

    @Test
    void equalityUsesId() {
        assertEquals(
                new ItemSpec(1L, ItemType.BOOK, "A", "A"),
                new ItemSpec(1L, ItemType.TECHITEM, "B", "B")
        );
    }

    @Test
    void convertsToAndFromEntity() {
        ItemSpecEntity entity = new ItemSpecEntity(1L, ItemType.PLANT, "Populus alba", "Desc");

        ItemSpec spec = ItemSpec.fromEntity(entity);

        assertEquals(entity.toString(), spec.toEntity().toString());
    }
}
