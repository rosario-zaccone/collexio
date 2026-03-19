package org.collexio;

import org.collexio.persistence.dao.DBItemSpecDAO;
import org.collexio.persistence.model.ItemSpecEntity;
import org.collexio.persistence.model.ItemType;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBItemSpecDAOTest {

    private static Connection connection;
    private DBItemSpecDAO dao;

    @BeforeAll
    void setupDatabase() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE item_specs(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "type INTEGER NOT NULL," +
                    "name TEXT NOT NULL," +
                    "description TEXT NOT NULL)");
        }
        dao = new DBItemSpecDAO(connection);
    }

    @AfterAll
    static void closeConnection() throws Exception {
        connection.close();
    }

    @BeforeEach
    void clearData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_specs;");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_specs';");
        }
    }

    @Test
    void testAddAndGet() throws Exception {
        ItemSpecEntity spec = new ItemSpecEntity(ItemType.BOOK, "Name", "Desc");
        ItemSpecEntity spec2 = new ItemSpecEntity(ItemType.BOOK, "Name2", "Desc");
        ItemSpecEntity spec3 = new ItemSpecEntity(ItemType.BOOK, "Name3", "Desc");
        dao.add(spec); dao.add(spec2); dao.add(spec3);
        ItemSpecEntity fetched = dao.get(2L).get();
        assertEquals(spec2.toStringNoId(), fetched.toStringNoId());
    }

    @Test
    void testUpdate() throws Exception {
        ItemSpecEntity spec = new ItemSpecEntity(ItemType.BOOK, "Name", "Desc");
        ItemSpecEntity spec2 = new ItemSpecEntity(ItemType.BOOK, "Name2", "Desc");
        ItemSpecEntity spec3 = new ItemSpecEntity(ItemType.BOOK, "Name3", "Desc");
        dao.add(spec); dao.add(spec2); dao.add(spec3);

        ItemSpecEntity saved = dao.getAll().get(0);
        ItemSpecEntity updated = new ItemSpecEntity(saved.getId(), saved.getType(), "Updated", saved.getDescription());

        dao.update(updated);

        Optional<ItemSpecEntity> updated2 = dao.get(saved.getId());
        assertEquals(updated.toStringNoId(), updated2.get().toStringNoId());
    }

    @Test
    void testDelete() throws Exception {
        ItemSpecEntity spec = new ItemSpecEntity(ItemType.BOOK, "Name", "Desc");
        ItemSpecEntity spec2 = new ItemSpecEntity(ItemType.BOOK, "Name2", "Desc");
        ItemSpecEntity spec3 = new ItemSpecEntity(ItemType.BOOK, "Name3", "Desc");
        dao.add(spec); dao.add(spec2); dao.add(spec3);

        ItemSpecEntity saved = dao.getAll().get(0);
        dao.delete(saved.getId());

        Optional<ItemSpecEntity> result = dao.get(saved.getId());
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAll() throws Exception {
        dao.add(new ItemSpecEntity(null, ItemType.BOOK, "A", "A"));
        dao.add(new ItemSpecEntity(null, ItemType.BOOK, "B", "B"));

        List<ItemSpecEntity> all = dao.getAll();
        assertEquals(2, all.size());
    }
}