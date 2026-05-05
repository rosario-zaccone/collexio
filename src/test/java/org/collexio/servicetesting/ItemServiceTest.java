package org.collexio.servicetesting;

import org.collexio.business.domain.*;
import org.collexio.business.service.*;
import org.collexio.persistence.dao.*;
import org.collexio.persistence.entity.*;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServiceTest {

    private Connection connection;
    private DBItemDAO itemDAO;
    private ItemService itemService;
    private ItemSpecService specService;
    private ItemCollectionService collectionService;

    @BeforeAll
    void setupDatabase() throws SQLException, IOException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("CREATE TABLE item_collections (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);");
            stmt.execute("""
                CREATE TABLE item_specs(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT NOT NULL
                );
            """);
            stmt.execute("""
                CREATE TABLE items(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    status INTEGER NOT NULL,
                    item_spec_id INTEGER,
                    item_collection_id INTEGER,
                    FOREIGN KEY(item_spec_id) REFERENCES item_specs(id) ON DELETE CASCADE,
                    FOREIGN KEY(item_collection_id) REFERENCES item_collections(id) ON DELETE SET NULL
                );
            """);
            stmt.execute("""
                CREATE TABLE item_photos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    path TEXT NOT NULL,
                    photo_date TEXT NOT NULL,
                    item_id INTEGER UNIQUE,
                    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
                );
            """);
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


        itemDAO = new DBItemDAO(connection);
        itemService = new ItemService(itemDAO);
        specService = new ItemSpecService(new DBItemSpecDAO(connection));
        collectionService = new ItemCollectionService(new DBItemCollectionDAO(connection));
    }

    @BeforeEach
    void clearData() throws SQLException, IOException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM items;");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='items';");
            stmt.execute("DELETE FROM item_specs;");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_specs';");
        }
    }

    @AfterAll
    void teardown() throws SQLException, IOException {
        connection.close();
    }

    @Test
    void testAddAndGetItem() throws SQLException, IOException {
        ItemSpec spec = specService.add(new ItemSpec(ItemType.TECHITEM, "nintendo ds", "blabla"));
        ItemCollection coll = collectionService.add(new ItemCollection("nintendo"));
        Item item = new Item(ItemStatus.GOOD, spec);
        item = itemService.add(item, coll.getId());
        Item fetched = itemService.get(item.getId());
        item.setSpec(null);
        assertEquals(item.toString(), fetched.toString());
    }

    @Test
    void testGetByCollectionId() throws SQLException, IOException {
        ItemSpec spec = specService.add(new ItemSpec(ItemType.TECHITEM, "nintendo ds", "blabla"));
        ItemCollection coll = collectionService.add(new ItemCollection("nintendo"));
        Item item1 = new Item(ItemStatus.GOOD, spec);
        item1 = itemService.add(item1, coll.getId());
        Item item2 = new Item(ItemStatus.GOOD, spec);
        item2 = itemService.add(item2, coll.getId());

        Item item3 = new Item(ItemStatus.GOOD, spec);
        item3 = itemService.add(item3, coll.getId());

        List<Item> items = itemService.getByCollectionId(coll.getId());

        assertEquals(3, items.size());
        assertTrue(items.contains(item1));
        assertTrue(items.contains(item2));
        assertTrue(items.contains(item3));
    }

    @Test
    void testDeleteItem() throws SQLException, IOException {
        ItemSpec spec = specService.add(new ItemSpec(ItemType.TECHITEM, "nintendo ds", "blabla"));
        ItemCollection coll = collectionService.add(new ItemCollection("nintendo"));
        Item item = new Item(ItemStatus.AVERAGE, spec);
        item = itemService.add(item, coll.getId());
        itemService.delete(item.getId());
        Item finalItem = item;
        assertThrows(NoSuchElementException.class, () -> itemService.get(finalItem.getId()));
    }

    @Test
    void testUpdateItem() throws SQLException, IOException {
        ItemSpec spec = specService.add(new ItemSpec(ItemType.TECHITEM, "nintendo ds", "blabla"));
        ItemCollection coll = collectionService.add(new ItemCollection("nintendo"));
        ItemCollection coll2 = collectionService.add(new ItemCollection("nintendo new"));
        Item item = new Item(ItemStatus.AVERAGE, spec);
        item = itemService.add(item, coll.getId());
        itemService.update(item, coll2.getId());
        assertEquals(0, collectionService.get(coll.getId()).getData().size());
    }
}
