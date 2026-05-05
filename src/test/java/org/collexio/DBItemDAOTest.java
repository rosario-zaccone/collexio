package org.collexio;

import org.collexio.persistence.dao.DBItemCollectionDAO;
import org.collexio.persistence.dao.DBItemDAO;
import org.collexio.persistence.dao.DBItemSpecDAO;
import org.collexio.persistence.entity.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBItemDAOTest {

    private Connection connection;
    private DBItemDAO itemDAO;
    ItemCollectionEntity c1 = new ItemCollectionEntity("Console");
    ItemCollectionEntity c2 = new ItemCollectionEntity("Console2");
    ItemSpecEntity s1 = new ItemSpecEntity(ItemType.TECHITEM, "Nintendo ds", "blabla");
    ItemSpecEntity s2 = new ItemSpecEntity(ItemType.TECHITEM, "Nintendo 3ds", "blabla");

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
                CREATE TABLE item_specs(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type INTEGER NOT NULL, -- 0 for plant, 1 for tech, 2 for book
                    name TEXT NOT NULL,
                    description TEXT NOT NULL
                );
            """);

            stmt.execute("""
                INSERT INTO item_specs (type, name, description)
                VALUES (1, 'nintendo ds', 'console nintendo'), (1, 'nintendo 3ds', 'console nintendo');
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

        itemDAO = new DBItemDAO(connection);
        var collectionDAO = new DBItemCollectionDAO(connection);
        var specDAO = new DBItemSpecDAO(connection);
        c1 = collectionDAO.add(c1);
        c2 = collectionDAO.add(c2);
        s1 =specDAO.add(s1);
        s2 = specDAO.add(s2);

    }

    @BeforeEach
    void clearData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM items;");

            stmt.execute("DELETE FROM sqlite_sequence WHERE name='items';");
        }
    }

    @AfterAll
    void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void testAddGet() throws SQLException {
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, s1);
        item = itemDAO.add(item, c1.getId());

        ItemEntity item2 = new ItemEntity(ItemStatus.GOOD, s1);
        item2 = itemDAO.add(item2, c1.getId());

        ItemEntity item3 = new ItemEntity(ItemStatus.BAD, s2);
        item3 = itemDAO.add(item3, null);

        ItemEntity fetched = itemDAO.get(item2.getId()).get();
        assertEquals(item2.getStatus(), fetched.getStatus());
    }

    @Test
    void testGetByCollectionId() throws SQLException {
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, s1);
        item = itemDAO.add(item, c1.getId());

        ItemEntity item2 = new ItemEntity(ItemStatus.AVERAGE, s1);
        item2 = itemDAO.add(item2,  c1.getId());

        ItemEntity item3 = new ItemEntity(ItemStatus.AVERAGE, s2);
        item3 = itemDAO.add(item3,  c2.getId());

        List<ItemEntity> items = itemDAO.getByCollectionId(c1.getId());
        assertEquals(2, items.size());
        System.out.println(items);
        items = itemDAO.getByCollectionId(c2.getId());
        assertEquals(1, items.size());
    }

    @Test
    void testUpdateItem() throws SQLException {
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, s1);
        itemDAO.add(item, c1.getId());

        ItemEntity updated = new ItemEntity(1L, ItemStatus.GOOD, s1);
        itemDAO.update(updated,c2.getId());

        ItemEntity fetched = itemDAO.get(1L).get();
        assertEquals(updated.getStatus(), fetched.getStatus());

        List<ItemEntity> items = itemDAO.getByCollectionId(c1.getId());
        assertEquals(items.size(), 0);
    }

    @Test
    void testDeleteItem() throws SQLException {
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, s1);

        itemDAO.add(item, c1.getId());

        ItemEntity saved = itemDAO.getAll().get(0);
        itemDAO.delete(saved.getId());

        Optional<ItemEntity> fetched = itemDAO.get(saved.getId());
        assertFalse(fetched.isPresent());
    }

}