package org.collexio;
import org.collexio.persistence.dao.*;
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
class DBItemCollectionDAOTest {

    private Connection connection;
    private DBItemSpecDAO itemSpecDAO;
    private DBItemDAO itemDAO;
    private DBItemPhotoDAO photoDAO;
    private DBTransactionDAO transactionDAO;
    private DBItemCollectionDAO collectionDAO;

    @BeforeAll
    void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");

            // Collections
            stmt.execute("""
                CREATE TABLE item_collections (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                );
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
        collectionDAO = new DBItemCollectionDAO(connection, itemDAO);
    }

    @BeforeEach
    void clearData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_collections;");
            stmt.execute("DELETE FROM item_transactions;");
            stmt.execute("DELETE FROM item_photos;");
            stmt.execute("DELETE FROM items;");
            stmt.execute("DELETE FROM item_specs;");

            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_collections';");
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
    void testAddGetCollection() throws SQLException {
        ItemCollectionEntity collection = new ItemCollectionEntity(null, "Console");

        ItemSpecEntity details = new ItemSpecEntity(ItemType.TECHITEM, "Nintendo 3DS", "nintendo 3ds blu");
        itemSpecDAO.add(details);
        ItemPhotoEntity photo = new ItemPhotoEntity(Path.of("data/pippo.png"), LocalDate.now());
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, photo, itemSpecDAO.get(1L).get());
        item.addTransaction(new TransactionEntity(200, false, LocalDate.now()));
        item.addTransaction(new TransactionEntity(400, true, LocalDate.now()));
        collection.addItem(item);


        ItemSpecEntity details2 = new ItemSpecEntity(ItemType.TECHITEM, "Nintendo DS", "nintendo ds blu");
        itemSpecDAO.add(details2);
        ItemPhotoEntity photo2 = new ItemPhotoEntity(Path.of("data/pippo2.png"), LocalDate.now());
        ItemEntity item2 = new ItemEntity(ItemStatus.AVERAGE, photo2, itemSpecDAO.get(2L).get());
        item2.addTransaction(new TransactionEntity(200, false, LocalDate.now()));
        item2.addTransaction(new TransactionEntity(400, true, LocalDate.now()));
        collection.addItem(item2);


        collectionDAO.add(collection);

        ItemCollectionEntity coll = collectionDAO.get(1L).get();
        assertEquals(coll.toStringNoId(), collection.toStringNoId());
    }


    @Test
    void testUpdateCollection() throws SQLException {
        ItemCollectionEntity collection = new ItemCollectionEntity(null, "Old Name");
        collectionDAO.add(collection);

        ItemCollectionEntity saved = collectionDAO.getAll().get(0);

        collectionDAO.update(new ItemCollectionEntity(saved.getId(), "New Name"));

        Optional<ItemCollectionEntity> updated = collectionDAO.get(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals("New Name", updated.get().getName());
    }

    @Test
    void testDeleteCollection() throws SQLException {
        ItemCollectionEntity collection = new ItemCollectionEntity(null, "To Delete");
        collectionDAO.add(collection);

        ItemCollectionEntity saved = collectionDAO.getAll().get(0);

        collectionDAO.delete(saved.getId());

        Optional<ItemCollectionEntity> deleted = collectionDAO.get(saved.getId());
        assertFalse(deleted.isPresent());
    }

}