package org.collexio;


import org.collexio.persistence.entity.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;

class EntityTest {
    @Test
    void testCreateItemEntity() {
        ItemPhotoEntity photo = new ItemPhotoEntity(1L, Path.of("pippo.png"), LocalDate.now());
        ItemSpecEntity detail = new ItemSpecEntity(1L, ItemType.TECHITEM, "NintendoDS", "desc");
        ItemEntity itementity = new ItemEntity(1L, ItemStatus.GOOD, photo, detail);

        itementity.addTransaction(new TransactionEntity(1L, 50, true, LocalDate.now()));
        itementity.addTransaction(new TransactionEntity(2L, 100, true, LocalDate.now()));
        itementity.addTransaction(new TransactionEntity(3L, 50, false, LocalDate.now()));
        itementity.removeTransaction(new TransactionEntity(1L, 50, true, LocalDate.now()));

        System.out.println(itementity);
    }

    @Test
    void testCreateCollectionEntity() {
        ItemPhotoEntity photo = new ItemPhotoEntity(1L, Path.of("pippo.png"), LocalDate.now());
        ItemSpecEntity detail = new ItemSpecEntity(1L, ItemType.TECHITEM, "NintendoDS", "desc");
        ItemEntity itementity1 = new ItemEntity(1L, ItemStatus.GOOD, photo, detail);

        itementity1.addTransaction(new TransactionEntity(1L, 50, true, LocalDate.now()));
        itementity1.addTransaction(new TransactionEntity(2L, 100, true, LocalDate.now()));
        itementity1.addTransaction(new TransactionEntity(3L, 50, false, LocalDate.now()));

        ItemPhotoEntity photo2 = new ItemPhotoEntity(2L, Path.of("pippo.png"), LocalDate.now());
        ItemSpecEntity detail2 = new ItemSpecEntity(2L, ItemType.TECHITEM, "NintendoDSLite", "desc");
        ItemEntity itementity2 = new ItemEntity(2L, ItemStatus.GOOD, photo2, detail2);

        itementity2.addTransaction(new TransactionEntity(4L, 50, true, LocalDate.now()));
        itementity2.addTransaction(new TransactionEntity(5L, 100, true, LocalDate.now()));
        itementity2.addTransaction(new TransactionEntity(6L, 500, false, LocalDate.now()));

        ItemCollectionEntity coll = new ItemCollectionEntity(1L, "console");
        coll.addItem(itementity1);
        coll.addItem(itementity2);

        System.out.println(coll);
    }
}