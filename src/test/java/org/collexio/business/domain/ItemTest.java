package org.collexio.business.domain;

import org.collexio.persistence.entity.ItemEntity;
import org.collexio.persistence.entity.ItemPhotoEntity;
import org.collexio.persistence.entity.ItemSpecEntity;
import org.collexio.persistence.entity.ItemStatus;
import org.collexio.persistence.entity.ItemType;
import org.collexio.persistence.entity.TransactionEntity;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void rejectsNonPositiveId() {
        assertThrows(IllegalArgumentException.class, () -> new Item(0L, ItemStatus.GOOD, null, null));
    }

    @Test
    void computesBalanceAndAvailabilityFromTransactions() {
        Item item = new Item(1L, ItemStatus.GOOD, null, spec());

        item.addTransaction(new Transaction(1L, 100, false, LocalDate.of(2024, 1, 1)));
        item.addTransaction(new Transaction(2L, 40, true, LocalDate.of(2024, 1, 2)));

        assertEquals(-60, item.balance());
        assertFalse(item.isAvailable());
    }

    @Test
    void returnsUnmodifiableTransactions() {
        Item item = new Item(ItemStatus.GOOD, spec());

        assertThrows(UnsupportedOperationException.class,
                () -> item.getTransactions().add(new Transaction(1, true, LocalDate.now())));
    }

    @Test
    void copiesPhotoOnSetAndGet() {
        Item item = new Item(ItemStatus.GOOD, spec());
        ItemPhoto photo = new ItemPhoto(1L, Path.of("a.png"), LocalDate.of(2024, 1, 1));

        item.setPhoto(photo);
        photo.setPath(Path.of("b.png"));

        assertEquals(Path.of("a.png"), item.getPhoto().getPath());
    }

    @Test
    void convertsToAndFromEntity() {
        ItemEntity entity = new ItemEntity(
                1L,
                ItemStatus.AVERAGE,
                new ItemPhotoEntity(2L, Path.of("a.png"), LocalDate.of(2024, 1, 1)),
                new ItemSpecEntity(3L, ItemType.BOOK, "Book", "Desc")
        );
        entity.addTransaction(new TransactionEntity(4L, 12, true, LocalDate.of(2024, 1, 2)));

        Item item = Item.fromEntity(entity);
        ItemEntity converted = item.toEntity();

        assertEquals(entity.toString(), converted.toString());
    }

    private ItemSpec spec() {
        return new ItemSpec(1L, ItemType.TECHITEM, "Nintendo", "Desc");
    }
}
