package org.collexio.servicetesting;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemCollection;
import org.collexio.business.domain.ItemSpec;
import org.collexio.business.service.ItemCollectionOrchestrator;
import org.collexio.business.service.ItemCollectionService;
import org.collexio.business.service.ItemService;
import org.collexio.business.service.ItemSpecService;
import org.collexio.persistence.dao.DBItemCollectionDAO;
import org.collexio.persistence.dao.DBItemDAO;
import org.collexio.persistence.dao.DBItemSpecDAO;
import org.collexio.persistence.entity.ItemStatus;
import org.collexio.persistence.entity.ItemType;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemCollectionOrchestratorTest {
    private Connection connection;
    private ItemCollectionOrchestrator service;

    private ItemService itemService;
    private ItemCollectionService collectionService;
    private ItemSpecService specService;

    @BeforeAll
    void setup() throws SQLException {
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
                    INSERT INTO item_collections (name)
                    VALUES ('Console');
                """);
        }

        itemService = new ItemService(new DBItemDAO(connection));
        collectionService = new ItemCollectionService(new DBItemCollectionDAO(connection));
        specService = new ItemSpecService(new DBItemSpecDAO(connection));

        service = new ItemCollectionOrchestrator(itemService, collectionService);
    }

    @BeforeEach
    void clear() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM items;");
            stmt.execute("DELETE FROM item_specs;");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='items';");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_specs';");
        }
    }

    @AfterAll
    void teardown() throws SQLException {
        connection.close();
    }

    @Test
    void getFullCollectionTest() throws SQLException, IOException {
        ItemSpec spec1 = specService.add(new ItemSpec(ItemType.TECHITEM, "Nintendo DS", "a"));
        ItemSpec spec2 = specService.add(new ItemSpec(ItemType.TECHITEM, "PSP", "b"));

        itemService.add(new Item(ItemStatus.GOOD, spec1), 1L);
        itemService.add(new Item(ItemStatus.GOOD, spec2), 1L);

        ItemCollection collection = service.getFullCollection(1L);

        assertEquals(2, collection.getData().size());
    }
}