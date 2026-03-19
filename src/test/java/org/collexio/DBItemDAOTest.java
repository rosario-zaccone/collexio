package org.collexio;

import org.collexio.persistence.dao.DBItemDAO;
import org.collexio.persistence.dao.DBItemPhotoDAO;
import org.collexio.persistence.dao.DBItemSpecDAO;
import org.collexio.persistence.dao.DBTransactionDAO;
import org.collexio.persistence.model.*;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBItemDAOTest {

    private Connection connection;
    private DBItemDAO itemDAO;
    private DBItemPhotoDAO photoDAO;
    private DBTransactionDAO transactionDAO;
    private DBItemSpecDAO itemSpecDAO;

    @BeforeAll
    void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("""
                CREATE TABLE item_collections (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                );
            """);

            stmt.execute("""
                INSERT INTO item_collections (name)
                VALUES ('Collection 1'), ('Collection 2');
            """);

            stmt.execute("""
                CREATE TABLE item_specs(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type INTEGER NOT NULL, -- 0 for plant, 1 for tech, 2 for book
                    name TEXT NOT NULL,
                    description TEXT NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE items(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    status INTEGER NOT NULL, -- 0 for bad 1 for average 2 for good
                    item_spec_id INTEGER,
                    item_collection_id INTEGER,
                    FOREIGN KEY(item_spec_id) REFERENCES item_specs(id) ON DELETE CASCADE ON UPDATE CASCADE,
                    FOREIGN KEY(item_collection_id) REFERENCES item_collections(id) ON DELETE SET NULL ON UPDATE CASCADE
                );
            """);

            stmt.execute("""
                CREATE TABLE item_photos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    path TEXT NOT NULL,
                    photo_date TEXT NOT NULL,
                    item_id INTEGER UNIQUE ,
                    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE ON UPDATE CASCADE
                );
            """);

            stmt.execute("""
                CREATE TABLE item_transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    amount REAL NOT NULL,
                    income INTEGER NOT NULL, -- boolean
                    transaction_date TEXT NOT NULL,
                    item_id INTEGER,
                    FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE ON UPDATE CASCADE
                );
            """);
        }

        photoDAO = new DBItemPhotoDAO(connection);
        transactionDAO = new DBTransactionDAO(connection);
        itemSpecDAO = new DBItemSpecDAO(connection);
        itemDAO = new DBItemDAO(connection, itemSpecDAO, photoDAO, transactionDAO);
    }

    @BeforeEach
    void clearData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_transactions;");
            stmt.execute("DELETE FROM item_photos;");
            stmt.execute("DELETE FROM items;");
            stmt.execute("DELETE FROM item_specs;");

            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_transactions';");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_photos';");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='items';");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_specs';");
        }
    }

    @AfterAll
    void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void testAddItem() throws SQLException {
        ItemSpecEntity details = new ItemSpecEntity(ItemType.TECHITEM, "Nintendo 3DS", "nintendo 3ds blu");
        itemSpecDAO.add(details);
        ItemPhotoEntity photo = new ItemPhotoEntity(Path.of("data/pippo.png"), LocalDate.now());
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, photo, itemSpecDAO.get(1L).get());
        item.addTransaction(new TransactionEntity(200, false, LocalDate.now()));
        item.addTransaction(new TransactionEntity(400, true, LocalDate.now()));
        itemDAO.add(item, 1L);

        ItemPhotoEntity photo2 = new ItemPhotoEntity(Path.of("data/photo2.png"), LocalDate.now());
        ItemEntity item2 = new ItemEntity(ItemStatus.GOOD, photo2, itemSpecDAO.get(1L).get());
        item2.addTransaction(new TransactionEntity(150, true, LocalDate.now()));
        itemDAO.add(item2, 1L);

        ItemPhotoEntity photo3 = new ItemPhotoEntity(Path.of("data/photo3.png"), LocalDate.now());
        ItemEntity item3 = new ItemEntity(ItemStatus.BAD, photo3, itemSpecDAO.get(1L).get());
        item3.addTransaction(new TransactionEntity(50, false, LocalDate.now()));
        itemDAO.add(item3, 1L);

        ItemEntity fetched = itemDAO.get(2L).get();
        assertEquals(item2.toStringNoId(), fetched.toStringNoId());
    }

    @Test
    void testGetByCollectionId() throws SQLException {
        ItemSpecEntity details = new ItemSpecEntity(1L, ItemType.TECHITEM, "Nintendo 3DS", "nintendo 3ds rosso");
        itemSpecDAO.add(details);
        ItemPhotoEntity photo = new ItemPhotoEntity(Path.of("data/pippo.png"), LocalDate.now());
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, photo, details);
        item.addTransaction(new TransactionEntity(200, false, LocalDate.now()));
        item.addTransaction(new TransactionEntity(400, true, LocalDate.now()));
        itemDAO.add(item, 1L);

        ItemSpecEntity details2 = new ItemSpecEntity(2L, ItemType.TECHITEM, "Nintendo DS", "nintendo ds bianco");
        itemSpecDAO.add(details2);
        ItemPhotoEntity photo2 = new ItemPhotoEntity(Path.of("data/pippo.png"), LocalDate.now());
        ItemEntity item2 = new ItemEntity(ItemStatus.AVERAGE, photo, details2);
        item2.addTransaction(new TransactionEntity(100, false, LocalDate.now()));
        item2.addTransaction(new TransactionEntity(200, true, LocalDate.now()));
        itemDAO.add(item2, 1L);

        List<ItemEntity> items = itemDAO.getByCollectionId(1L);
        assertEquals(2, items.size());
        System.out.println(items);
    }

    @Test
    void testUpdateItem() throws SQLException {
        ItemSpecEntity details = new ItemSpecEntity(1L, ItemType.BOOK, "Libro", "desc");
        ItemPhotoEntity photo = new ItemPhotoEntity(Path.of("/tmp/photo4.jpg"), LocalDate.now());
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, photo, details);
        item.addTransaction(new TransactionEntity(15.0, true, LocalDate.now()));
        itemSpecDAO.add(details);
        itemDAO.add(item, 1L);

        ItemEntity updated = new ItemEntity(1L, ItemStatus.GOOD, photo, details);
        updated.addTransaction(new TransactionEntity(15.0, true, LocalDate.now()));

        itemDAO.update(updated, false);

        ItemEntity fetched = itemDAO.get(1L).get();
        assertEquals(updated.toStringNoId(), fetched.toStringNoId());
    }

    @Test
    void testDeleteItem() throws SQLException {
        ItemSpecEntity details = new ItemSpecEntity(1L, ItemType.TECHITEM, "Nintendo DS");
        itemSpecDAO.add(details);
        ItemPhotoEntity photo = new ItemPhotoEntity(Path.of("data/pippo.png"), LocalDate.now());
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, photo, details);
        item.addTransaction(new TransactionEntity(200, false, LocalDate.now()));
        item.addTransaction(new TransactionEntity(400, true, LocalDate.now()));

        itemDAO.add(item, 1L);

        ItemEntity saved = itemDAO.getAll().get(0);
        itemDAO.delete(saved.getId());

        Optional<ItemEntity> fetched = itemDAO.get(saved.getId());
        assertFalse(fetched.isPresent());
    }

}