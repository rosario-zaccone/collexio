package org.collexio;

import org.collexio.persistence.dao.DBItemCollectionDAO;
import org.collexio.persistence.dao.DBItemDAO;
import org.collexio.persistence.dao.DBItemPhotoDAO;
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
class DBItemCollectionDAOTest {

    private Connection connection;
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

            // Items
            stmt.execute("""
                CREATE TABLE items (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    quantity INTEGER NOT NULL,
                    description TEXT NOT NULL,
                    type INTEGER NOT NULL,
                    item_collection_id INTEGER,
                    FOREIGN KEY(item_collection_id) REFERENCES item_collections(id) ON DELETE SET NULL
                );
            """);

            // Photos
            stmt.execute("""
                CREATE TABLE item_photos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    path TEXT NOT NULL,
                    photo_date TEXT NOT NULL,
                    item_id INTEGER,
                    FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE
                );
            """);

            // Transactions
            stmt.execute("""
                CREATE TABLE item_transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    amount REAL NOT NULL,
                    income INTEGER NOT NULL,
                    transaction_date TEXT NOT NULL,
                    item_id INTEGER,
                    FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE
                );
            """);
        }

        photoDAO = new DBItemPhotoDAO(connection);
        transactionDAO = new DBTransactionDAO(connection);
        itemDAO = new DBItemDAO(connection, photoDAO, transactionDAO);
        collectionDAO = new DBItemCollectionDAO(connection, itemDAO);
    }

    @BeforeEach
    void clearData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_transactions;");
            stmt.execute("DELETE FROM item_photos;");
            stmt.execute("DELETE FROM items;");
            stmt.execute("DELETE FROM item_collections;");
        }
    }

    @AfterAll
    void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void testAddCollection() throws SQLException {
        ItemCollectionEntity collection = new ItemCollectionEntity(null, "Console");

        ItemPhotoEntity photo = new ItemPhotoEntity(Path.of("/tmp/photo.jpg"), LocalDate.now());
        ItemEntity item = new ItemEntity(null, ItemType.TECHITEM, "GameBoy", 2, photo, "desc");
        item.addTransaction(new TransactionEntity(50, true, LocalDate.now()));

        ItemPhotoEntity photo2 = new ItemPhotoEntity(Path.of("/tmp/photo2.jpg"), LocalDate.now());
        ItemEntity item2 = new ItemEntity(null, ItemType.TECHITEM, "GameBoy Advance", 2, photo, "desc");
        item2.addTransaction(new TransactionEntity(50, true, LocalDate.now()));

        collection.addItem(item); collection.addItem(item2);

        collectionDAO.add(collection);

        List<ItemCollectionEntity> all = collectionDAO.getAll();
        assertEquals(1, all.size());
        assertEquals("Console", all.get(0).getName());
        assertEquals(2, all.get(0).getData().size());
        System.out.println(all.get(0));
    }

    @Test
    void testGetCollection() throws SQLException {
        ItemCollectionEntity collection = new ItemCollectionEntity(null, "Collection A");

        ItemPhotoEntity photo = new ItemPhotoEntity(Path.of("/tmp/photo2.jpg"), LocalDate.now());
        ItemEntity item = new ItemEntity(null, ItemType.BOOK, "Book1", 1, photo, "desc");
        collection.addItem(item);

        collectionDAO.add(collection);

        ItemCollectionEntity saved = collectionDAO.getAll().get(0);

        Optional<ItemCollectionEntity> fetched = collectionDAO.get(saved.getId());

        assertTrue(fetched.isPresent());
        assertEquals("Collection A", fetched.get().getName());
        assertEquals(1, fetched.get().getData().size());
        System.out.println(fetched.get());
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

    @Test
    void testGetAllCollections() throws SQLException {
        collectionDAO.add(new ItemCollectionEntity(null, "C1"));
        collectionDAO.add(new ItemCollectionEntity(null, "C2"));

        List<ItemCollectionEntity> all = collectionDAO.getAll();

        assertEquals(2, all.size());
    }
}