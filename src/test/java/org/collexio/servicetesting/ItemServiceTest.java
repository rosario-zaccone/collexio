package org.collexio.servicetesting;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemPhoto;
import org.collexio.business.domain.ItemSpec;
import org.collexio.business.domain.Transaction;
import org.collexio.business.service.ItemPhotoService;
import org.collexio.business.service.ItemService;
import org.collexio.business.service.ItemSpecService;
import org.collexio.business.service.TransactionService;
import org.collexio.persistence.dao.DBItemDAO;
import org.collexio.persistence.dao.DBItemPhotoDAO;
import org.collexio.persistence.dao.DBItemSpecDAO;
import org.collexio.persistence.dao.DBTransactionDAO;
import org.collexio.persistence.model.*;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServiceTest {

    private Connection connection;
    private DBItemDAO itemDAO;
    private DBItemPhotoDAO photoDAO;
    private DBTransactionDAO transactionDAO;
    private DBItemSpecDAO itemSpecDAO;
    private ItemService itemService;
    private ItemSpecService itemSpecService;

    private Path tempDir;

    @BeforeAll
    void setupDatabase() throws SQLException, IOException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("CREATE TABLE item_collections (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);");
            stmt.execute("INSERT INTO item_collections (name) VALUES ('Console');");
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

        itemSpecService = new ItemSpecService(itemSpecDAO);
        itemService = new ItemService(itemDAO, new ItemPhotoService(photoDAO), new TransactionService(transactionDAO), itemSpecService);
        // cartella temporanea per le foto
        tempDir = Files.createTempDirectory("item_photos");
        System.setProperty("ITEM_PHOTO_DIR", tempDir.toString());
    }

    @BeforeEach
    void clearData() throws SQLException, IOException {
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
    void testAddAndGetItem() throws SQLException, IOException {
        ItemSpec spec = new ItemSpec(ItemType.TECHITEM, "Nintendo Switch", "console");
        spec = itemSpecService.add(spec);

        Item item = new Item(ItemStatus.GOOD, new ItemPhoto(Path.of("images/test/test.png"), LocalDate.now()), spec);
        item.addTransaction(new Transaction(1L, 200, true, LocalDate.now()));
        item.addTransaction(new Transaction(2L, 400, true, LocalDate.now()));

        item = itemService.add(item, 1L);
        Item fetched = itemService.get(item.getId());
        assertEquals(item.toString(), fetched.toString());
    }

    @Test
    void testGetByCollectionId() throws SQLException, IOException {
        ItemSpecEntity spec = new ItemSpecEntity(ItemType.TECHITEM, "Nintendo Switch", "console");
        spec = itemSpecDAO.add(spec);

        Path photoPath1 = Files.createTempFile(tempDir, "photo1", ".png");
        ItemPhoto photo1 = new ItemPhoto(photoPath1, LocalDate.now());
        Item item1 = new Item(ItemStatus.GOOD, photo1, ItemSpec.fromEntity(spec));
        item1 = itemService.add(item1, 1L);

        Path photoPath2 = Files.createTempFile(tempDir, "photo2", ".png");
        ItemPhoto photo2 = new ItemPhoto(photoPath2, LocalDate.now());
        Item item2 = new Item(ItemStatus.GOOD, photo2, ItemSpec.fromEntity(spec));
        item2 = itemService.add(item2, 1L);

        Path photoPath3 = Files.createTempFile(tempDir, "photo3", ".png");
        ItemPhoto photo3 = new ItemPhoto(photoPath3, LocalDate.now());
        Item item3 = new Item(ItemStatus.GOOD, photo3, ItemSpec.fromEntity(spec));
        item3 = itemService.add(item3, 1L);

        List<Item> items = itemService.getByCollectionId(1L);

        assertEquals(3, items.size());
        assertTrue(items.contains(item1));
        assertTrue(items.contains(item2));
        assertTrue(items.contains(item3));
    }

    @Test
    void testDeleteItem() throws SQLException, IOException {
        ItemSpecEntity spec = new ItemSpecEntity(ItemType.TECHITEM, "GameCube", "console");
        spec = itemSpecDAO.add(spec);

        Path photoPath = Files.createTempFile(tempDir, "photo3", ".png");
        ItemPhoto photo = new ItemPhoto(photoPath, LocalDate.now());
        Item item = new Item(ItemStatus.AVERAGE, photo, ItemSpec.fromEntity(spec));

        item = itemService.add(item, 1L);
        itemService.delete(item.getId());

        assertThrows(NoSuchElementException.class, () -> itemService.get(1L));
    }
}
