package org.collexio;

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
class DBItemDAOTest {

    private Connection connection;
    private DBItemDAO itemDAO;
    private DBItemPhotoDAO photoDAO;
    private DBTransactionDAO transactionDAO;

    @BeforeAll
    void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");

            // Item collections
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

            // Items
            stmt.execute("""
                CREATE TABLE items (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    quantity INTEGER NOT NULL,
                    description TEXT,
                    type INTEGER NOT NULL,
                    item_collection_id INTEGER,
                    FOREIGN KEY(item_collection_id) REFERENCES item_collections(id) ON DELETE SET NULL
                );
            """);

            // Item photos
            stmt.execute("""
                CREATE TABLE item_photos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    path TEXT NOT NULL,
                    photo_date TEXT NOT NULL,
                    item_id INTEGER,
                    FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE
                );
            """);

            // Item transactions
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
    }

    @BeforeEach
    void clearData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM items;");
            stmt.execute("DELETE FROM item_transactions;");
            stmt.execute("DELETE FROM item_photos;");
        }
    }

    @AfterAll
    void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void testAddItem() throws SQLException {
        ItemPhotoEntity photo = new ItemPhotoEntity(Path.of("data/pippo.png"), LocalDate.now());
        ItemEntity item = new ItemEntity(null, ItemType.TECHITEM, "Nintendo ds", 1, photo, "nintendo ds blu metallico");
        item.addTransaction(new TransactionEntity(200, false, LocalDate.now()));
        item.addTransaction(new TransactionEntity(400, true, LocalDate.now()));

        itemDAO.add(item, 1L);
        ItemEntity fetched = itemDAO.getAll().get(0);
        assertEquals(1L, fetched.getId());
        assertEquals(fetched.getName(), item.getName());
        assertEquals(fetched.getTransactions().size(), item.getTransactions().size());

    }

    @Test
    void testGetByCollectionId() throws SQLException {
        ItemPhotoEntity photo = new ItemPhotoEntity(null, Path.of("/tmp/photo2.jpg"), LocalDate.now());
        ItemEntity item1 = new ItemEntity(null, ItemType.TECHITEM, "Nintendo 3DS", 7, photo, "Coll desc");
        item1.addTransaction(new TransactionEntity(110.0, true, LocalDate.now()));
        item1.addTransaction(new TransactionEntity(120.0, false, LocalDate.now()));

        itemDAO.add(item1, 1L);

        ItemEntity item2 = new ItemEntity(null, ItemType.BOOK, "PSP Vita", 4, photo, "Coll2 desc");
        item2.addTransaction(new TransactionEntity(115.0, true, LocalDate.now()));
        itemDAO.add(item2, 1L);

        List<ItemEntity> items = itemDAO.getByCollectionId(1L);

        assertEquals(2, items.size());
        System.out.println(items.get(0));
        System.out.println(items.get(1));
    }

    @Test
    void testUpdateItem() throws SQLException {
        ItemPhotoEntity photo = new ItemPhotoEntity(null, Path.of("/tmp/photo3.jpg"), LocalDate.now());
        ItemEntity item = new ItemEntity(null, ItemType.PLANT, "Updatable Item", 1, photo, "Old desc");
        item.addTransaction(new TransactionEntity(15.0, true, LocalDate.now()));
        itemDAO.add(item, 1L);

        ItemEntity saved = itemDAO.getAll().get(0);

        itemDAO.update(new ItemEntity(saved.getId(), saved.getType(), "Updated Name", 99, saved.getPhoto(), "New desc"), false);

        Optional<ItemEntity> fetched = itemDAO.get(saved.getId());
        assertTrue(fetched.isPresent());
        assertEquals("Updated Name", fetched.get().getName());
        assertEquals(99, fetched.get().getQuantity());
        assertEquals("New desc", fetched.get().getDescription());
    }

    @Test
    void testDeleteItem() throws SQLException {
        ItemPhotoEntity photo = new ItemPhotoEntity(null, Path.of("/tmp/photo4.jpg"), LocalDate.now());
        ItemEntity item = new ItemEntity(null, ItemType.TECHITEM, "Delete Me", 2, photo, "Desc");
        item.addTransaction(new TransactionEntity(30.0, false, LocalDate.now()));
        itemDAO.add(item, 1L);

        ItemEntity saved = itemDAO.getAll().get(0);
        itemDAO.delete(saved.getId());

        Optional<ItemEntity> fetched = itemDAO.get(saved.getId());
        assertFalse(fetched.isPresent());
    }
}