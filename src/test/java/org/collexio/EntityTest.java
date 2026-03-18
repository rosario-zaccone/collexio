package org.collexio;

import org.collexio.persistence.model.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void testTransactionEntity() {
        LocalDate date = LocalDate.of(2026, 3, 17);
        TransactionEntity t1 = new TransactionEntity(1L, 100.0, true, date);
        TransactionEntity t2 = new TransactionEntity(100.0, true, date);

        // Test getter
        assertEquals(1L, t1.getId());
        assertEquals(100.0, t1.getAmount());
        assertTrue(t1.isIncome());
        assertEquals(date, t1.getDate());

        // Test equals & hashCode
        TransactionEntity t1Copy = new TransactionEntity(1L, 200.0, false, date.plusDays(1));
        assertEquals(t1, t1Copy);
        assertEquals(t1.hashCode(), t1Copy.hashCode());
        assertNotEquals(t1, t2);

        // Test compareTo
        TransactionEntity t3 = new TransactionEntity(2L, 50.0, false, date.plusDays(1));
        assertTrue(t1.compareTo(t3) < 0);
        assertTrue(t3.compareTo(t1) > 0);

        // Test toString
        assertTrue(t1.toString().contains("id='1'"));
    }

    @Test
    void testItemTypeEnum() {
        assertEquals(ItemType.PLANT, ItemType.fromInt(0));
        assertEquals(ItemType.TECHITEM, ItemType.fromInt(1));
        assertEquals(ItemType.BOOK, ItemType.fromInt(2));
        assertThrows(IllegalArgumentException.class, () -> ItemType.fromInt(99));
    }

    @Test
    void testItemPhotoEntity() {
        LocalDate date = LocalDate.of(2026, 3, 17);
        Path path = Path.of("/tmp/photo.jpg");
        ItemPhotoEntity photo1 = new ItemPhotoEntity(path, date);
        ItemPhotoEntity photo2 = new ItemPhotoEntity(path, date);

        // Test getter
        assertEquals(path, photo1.getPath());
        assertEquals(date, photo1.getDate());

        // Test equals & hashCode
        assertEquals(photo1, photo2);
        assertEquals(photo1.hashCode(), photo2.hashCode());

        // Test compareTo
        ItemPhotoEntity photo3 = new ItemPhotoEntity(Path.of("/tmp/another.jpg"), date.plusDays(1));
        assertTrue(photo1.compareTo(photo3) < 0);
    }

    @Test
    void testItemEntity() {
        LocalDate date = LocalDate.of(2026, 3, 17);
        ItemPhotoEntity photo = new ItemPhotoEntity(Path.of("/tmp/item.jpg"), date);

        ItemEntity item = new ItemEntity(1L, ItemType.BOOK, "Java Book", 5, photo, "A programming book");
        TransactionEntity transaction = new TransactionEntity(200.0, true, date);

        item.addTransaction(transaction);

        // Test getters
        assertEquals(1L, item.getId());
        assertEquals(ItemType.BOOK, item.getType());
        assertEquals("Java Book", item.getName());
        assertEquals(5, item.getQuantity());
        assertEquals(photo, item.getPhoto());
        assertEquals("A programming book", item.getDescription());

        List<TransactionEntity> transactions = item.getTransactions();
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));

        // Test toString
        assertTrue(item.toString().contains("Java Book"));
    }

    @Test
    void testItemCollectionEntity() {
        ItemCollectionEntity collection = new ItemCollectionEntity("My Collection");
        ItemEntity item1 = new ItemEntity(1L, ItemType.PLANT, "Cactus", 3, null, "Small cactus");
        ItemEntity item2 = new ItemEntity(2L, ItemType.TECHITEM, "Keyboard", 2, null, "Mechanical keyboard");

        collection.addItem(item1);
        collection.addItem(item2);

        List<ItemEntity> items = collection.getData();
        assertEquals(2, items.size());
        assertTrue(items.contains(item1));
        assertTrue(items.contains(item2));

        collection.removeItem(item1);
        assertEquals(1, collection.getData().size());
        assertFalse(collection.getData().contains(item1));

        // Test toString
        assertTrue(collection.toString().contains("My Collection"));
    }
}