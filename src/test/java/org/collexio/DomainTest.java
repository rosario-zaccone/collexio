package org.collexio;
import org.collexio.business.domain.*;
import org.collexio.persistence.model.ItemStatus;
import org.collexio.persistence.model.ItemType;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DomainTest {
    @Test
    void testCreateItem() {
        ItemPhoto photo = new ItemPhoto(1L, Path.of("pippo.png"), LocalDate.now());
        ItemSpec spec = new ItemSpec(1L, ItemType.TECHITEM, "NintendoDS", "desc");
        Item item = new Item(1L, ItemStatus.GOOD, photo, spec);
        item.setStatus(ItemStatus.AVERAGE);
        item.addTransaction(new Transaction(1L, 50, true, LocalDate.now()));
        item.addTransaction(new Transaction(2L, 100, true, LocalDate.now()));
        item.addTransaction(new Transaction(3L, 50, false, LocalDate.now()));
        item.removeTransaction(new Transaction(1L, 50, true, LocalDate.now()));
        System.out.println(item);
        assertEquals(50, item.balance());
    }

    @Test
    void testCreateCollection() {
        ItemPhoto photo = new ItemPhoto(1L, Path.of("pippo.png"), LocalDate.now());
        ItemSpec spec = new ItemSpec(1L, ItemType.TECHITEM, "NintendoDS", "desc");
        Item item = new Item(1L, ItemStatus.GOOD, photo, spec);
        item.addTransaction(new Transaction(1L, 50, true, LocalDate.now()));
        item.addTransaction(new Transaction(2L, 100, true, LocalDate.now()));
        item.addTransaction(new Transaction(3L, 50, false, LocalDate.now()));

        ItemPhoto photo2 = new ItemPhoto(2L, Path.of("pippo.png"), LocalDate.now());
        ItemSpec spec2 = new ItemSpec(2L, ItemType.TECHITEM, "NintendoDSLite", "desc");
        Item item2 = new Item(2L, ItemStatus.GOOD, photo2, spec2);
        item2.addTransaction(new Transaction(4L, 50, true, LocalDate.now()));
        item2.addTransaction(new Transaction(5L, 100, true, LocalDate.now()));
        item2.addTransaction(new Transaction(6L, 500, false, LocalDate.now()));

        ItemCollection coll = new ItemCollection(1L, "console");
        coll.addItem(item);
        coll.addItem(item2);
        assertEquals(-250, coll.totalBalance());
        assertEquals(2, coll.totalQuantity());
    }
}