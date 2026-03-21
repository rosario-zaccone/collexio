package org.collexio.servicetesting;

import org.collexio.business.domain.*;
import org.collexio.business.service.*;
import org.collexio.persistence.dao.*;
import org.collexio.persistence.model.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemCollectionServiceTest {

    private Connection connection;
    private DBItemDAO itemDAO;
    private DBItemPhotoDAO photoDAO;
    private DBTransactionDAO transactionDAO;
    private DBItemSpecDAO itemSpecDAO;
    private DBItemCollectionDAO collectionDAO;

    private ItemService itemService;
    private ItemSpecService itemSpecService;
    private ItemCollectionService collectionService;

    private Path tempDir;

    @BeforeAll
    void setup() throws SQLException, IOException {
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

        photoDAO = new DBItemPhotoDAO(connection);
        transactionDAO = new DBTransactionDAO(connection);
        itemSpecDAO = new DBItemSpecDAO(connection);
        itemDAO = new DBItemDAO(connection);
        collectionDAO = new DBItemCollectionDAO(connection);

        itemSpecService = new ItemSpecService(itemSpecDAO);
        itemService = new ItemService(itemDAO, new ItemPhotoService(photoDAO), new TransactionService(transactionDAO), itemSpecService);
        collectionService = new ItemCollectionService(collectionDAO, itemService);

        tempDir = Files.createTempDirectory("photos");
        System.setProperty("ITEM_PHOTO_DIR", tempDir.toString());
    }

    @BeforeEach
    void clear() throws SQLException, IOException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_transactions;");
            stmt.execute("DELETE FROM item_photos;");
            stmt.execute("DELETE FROM items;");
            stmt.execute("DELETE FROM item_specs;");
            stmt.execute("DELETE FROM item_collections;");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_collections';");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_transactions';");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_photos';");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='items';");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_specs';");
        }

        Files.walk(tempDir)
                .filter(Files::isRegularFile)
                .forEach(f -> f.toFile().delete());
    }

    @AfterAll
    void teardown() throws SQLException, IOException {
        connection.close();
        Files.walk(tempDir)
                .sorted((a,b) -> b.compareTo(a))
                .forEach(f -> f.toFile().delete());
    }

    @Test
    void testAddAndGetCollection() throws SQLException, IOException {
        ItemSpec spec = itemSpecService.add(new ItemSpec(ItemType.TECHITEM, "Switch", "console"));

        Path p1 = Files.createTempFile(tempDir, "p1", ".png");
        Path p2 = Files.createTempFile(tempDir, "p2", ".png");

        Item i1 = new Item(ItemStatus.GOOD, new ItemPhoto(p1, LocalDate.now()), spec);
        Item i2 = new Item(ItemStatus.AVERAGE, new ItemPhoto(p2, LocalDate.now()), spec);

        ItemCollection col = new ItemCollection("Test");
        col.addItem(i1);
        col.addItem(i2);

        col = collectionService.add(col);

        ItemCollection fetched = collectionService.get(col.getId());

        assertEquals(2, fetched.getData().size());
    }

    @Test
    void testGetAll() throws SQLException, IOException {
        ItemCollection c1 = collectionService.add(new ItemCollection("A"));
        ItemCollection c2 = collectionService.add(new ItemCollection("B"));

        List<ItemCollection> all = collectionService.getAll();

        assertEquals(2, all.size());
    }

    @Test
    void testDelete() throws SQLException, IOException {
        ItemCollection col = collectionService.add(new ItemCollection("ToDelete"));
        collectionService.delete(col.getId());

        assertThrows(NoSuchElementException.class, () -> collectionService.get(col.getId()));
    }
}