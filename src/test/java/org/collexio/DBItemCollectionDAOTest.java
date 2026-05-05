package org.collexio;
import org.collexio.persistence.dao.*;
import org.collexio.persistence.entity.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBItemCollectionDAOTest {

    private Connection connection;
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

        collectionDAO = new DBItemCollectionDAO(connection);
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
        collection = collectionDAO.add(collection);

        ItemCollectionEntity coll = collectionDAO.get(collection.getId()).get();
        assertEquals(coll.toStringNoId(), collection.toStringNoId());
    }


    @Test
    void testUpdateCollection() throws SQLException {
        ItemCollectionEntity collection = new ItemCollectionEntity(null, "Old Name");
        collection = collectionDAO.add(collection);

        ItemCollectionEntity saved = collectionDAO.get(collection.getId()).get();

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