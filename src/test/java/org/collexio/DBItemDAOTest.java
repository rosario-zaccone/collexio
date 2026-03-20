package org.collexio;

import org.collexio.persistence.dao.DBItemDAO;
import org.collexio.persistence.model.*;
import org.junit.jupiter.api.Disabled;
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
        ItemSpecEntity spec = new ItemSpecEntity(1L, ItemType.TECHITEM, "nintendo ds", "console nintendo");
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, spec);
        itemDAO.add(item, 1L);

        ItemEntity item2 = new ItemEntity(ItemStatus.GOOD, spec);
        itemDAO.add(item2, 1L);

        ItemEntity item3 = new ItemEntity(ItemStatus.BAD, spec);
        itemDAO.add(item3, 1L);

        ItemEntity fetched = itemDAO.get(2L).get();
        assertEquals(item2.getStatus(), fetched.getStatus());
    }

    @Test
    void testGetByCollectionId() throws SQLException {
        ItemSpecEntity spec = new ItemSpecEntity(1L, ItemType.TECHITEM, "nintendo ds", "console nintendo");
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, spec);
        itemDAO.add(item, 1L);

        ItemEntity item2 = new ItemEntity(ItemStatus.AVERAGE, spec);
        itemDAO.add(item2,  1L);

        List<ItemEntity> items = itemDAO.getByCollectionId(1L);
        assertEquals(2, items.size());
        System.out.println(items);
    }

    @Test
    void testUpdateItem() throws SQLException {
        ItemSpecEntity spec = new ItemSpecEntity(1L, ItemType.TECHITEM, "nintendo ds", "console nintendo");
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, spec);
        itemDAO.add(item, 1L);

        ItemEntity updated = new ItemEntity(1L, ItemStatus.GOOD, spec);
        itemDAO.update(updated, false);

        ItemEntity fetched = itemDAO.get(1L).get();
        assertEquals(updated.getStatus(), fetched.getStatus());
    }

    @Test
    void testDeleteItem() throws SQLException {
        ItemSpecEntity spec = new ItemSpecEntity(1L, ItemType.TECHITEM, "nintendo ds", "console nintendo");
        ItemEntity item = new ItemEntity(ItemStatus.AVERAGE, spec);

        itemDAO.add(item, 1L);

        ItemEntity saved = itemDAO.getAll().get(0);
        itemDAO.delete(saved.getId());

        Optional<ItemEntity> fetched = itemDAO.get(saved.getId());
        assertFalse(fetched.isPresent());
    }

}