package org.collexio.business.domain;

import org.collexio.persistence.entity.ItemCollectionEntity;
import org.collexio.persistence.entity.ItemEntity;
import org.collexio.persistence.entity.ItemStatus;
import org.collexio.persistence.entity.ItemSpecEntity;
import org.collexio.persistence.entity.ItemType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemCollectionTest {

    @Test
    void rejectsInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> new ItemCollection(0L, "Console"));
        assertThrows(IllegalArgumentException.class, () -> new ItemCollection(""));
    }

    @Test
    void tracksQuantityAndBalance() {
        Item first = item(1L, 100, true);
        Item second = item(2L, 30, false);
        ItemCollection collection = new ItemCollection(1L, "Console");

        collection.addItem(first);
        collection.addItem(second);

        assertEquals(2, collection.totalQuantity());
        assertEquals(70.0, collection.totalBalance());
        assertEquals(2, collection.length());
    }

    @Test
    void returnsUnmodifiableData() {
        ItemCollection collection = new ItemCollection("Console");

        assertThrows(UnsupportedOperationException.class, () -> collection.getData().add(item(1L, 1, true)));
    }

    @Test
    void convertsToAndFromEntity() {
        ItemCollectionEntity entity = new ItemCollectionEntity(1L, "Console");
        entity.addItem(new ItemEntity(2L, ItemStatus.GOOD, new ItemSpecEntity(3L, ItemType.TECHITEM, "DS", "Desc")));

        ItemCollection collection = ItemCollection.fromEntity(entity);
        ItemCollectionEntity converted = collection.toEntity();

        assertEquals(entity.toStringNoId(), converted.toStringNoId());
    }

    private Item item(Long id, double amount, boolean income) {
        Item item = new Item(id, ItemStatus.GOOD, null, new ItemSpec(1L, ItemType.TECHITEM, "DS", "Desc"));
        item.addTransaction(new Transaction(id, amount, income, LocalDate.of(2024, 1, 1)));
        return item;
    }
}
