package org.collexio.servicetesting;

import net.bytebuddy.asm.Advice;
import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemPhoto;
import org.collexio.business.domain.ItemSpec;
import org.collexio.business.domain.Transaction;
import org.collexio.business.service.*;
import org.collexio.persistence.dao.*;
import org.collexio.persistence.model.ItemStatus;
import org.collexio.persistence.model.ItemType;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemOrchestratorTest {
    private Connection connection;
    private ItemOrchestrator service;

    private ItemService itemService;
    private ItemPhotoService photoService;
    private TransactionService transactionService;
    private ItemSpecService specService;

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

            stmt.execute("""
                        INSERT INTO item_collections (name)
                        VALUES ('Console');
                    """);
        }

        itemService = new ItemService(new DBItemDAO(connection));
        photoService = new ItemPhotoService(new DBItemPhotoDAO(connection));
        transactionService = new TransactionService(new DBTransactionDAO(connection));
        specService = new ItemSpecService(new DBItemSpecDAO(connection));

        service = new ItemOrchestrator(
                itemService,
                photoService,
                transactionService,
                specService
        );
    }

    @BeforeEach
    void clear() throws SQLException, IOException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_specs;");
            stmt.execute("DELETE FROM item_photos;");
            stmt.execute("DELETE FROM item_transactions;");
            stmt.execute("DELETE FROM items;");

            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_specs';");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_photos';");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_transactions';");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='items';");
        }
    }

    @AfterAll
    void teardown() throws SQLException, IOException {
        connection.close();
    }

    @Test
    void getTest() throws SQLException, IOException {
        ItemSpec spec = specService.add(new ItemSpec(ItemType.TECHITEM, "Nintendo DS", "blabla"));
        Item item = service.addWithPhoto(new Item(ItemStatus.GOOD, spec), 1L, new ItemPhoto(Path.of("/home/rosario/Documents/computer_science/projects/collexio/images/test/test.png"), LocalDate.now()));
        transactionService.addTransaction(new Transaction(200, false, LocalDate.now()), item.getId());
        transactionService.addTransaction(new Transaction(250, true, LocalDate.now()), item.getId());
        item = service.getFullItem(item.getId());
        System.out.println(item.balance());
    }
}

