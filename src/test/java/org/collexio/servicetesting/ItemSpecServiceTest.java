package org.collexio.servicetesting;

import org.collexio.business.domain.ItemSpec;
import org.collexio.business.service.ItemSpecService;
import org.collexio.persistence.dao.DBItemSpecDAO;
import org.collexio.persistence.model.ItemType;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemSpecServiceTest {
    private static Connection connection;
    private DBItemSpecDAO dao;
    private ItemSpecService service;

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
        service = new ItemSpecService(dao);
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
    public void testAddGet() throws SQLException {
        ItemSpec spec = new ItemSpec(1L, ItemType.TECHITEM, "nintendo ds", "desc");
        service.add(spec);
        ItemSpec fetched = service.get(1L);
        assertEquals(spec.toEntity().toStringNoId(), fetched.toEntity().toStringNoId());

        assertThrows(NoSuchElementException.class, () -> service.get(2L));
    }

    @Test
    public void testDelete() throws SQLException {
        ItemSpec spec = new ItemSpec(1L, ItemType.TECHITEM, "nintendo ds", "desc");
        service.add(spec);
        service.delete(1L);

        assertThrows(NoSuchElementException.class, () -> service.get(1L));
    }

    @Test
    public void testUpdate() throws SQLException {
        ItemSpec spec = new ItemSpec(1L, ItemType.TECHITEM, "nintendo ds", "desc");
        service.add(spec);

        ItemSpec updatedSpec = new ItemSpec(1L, ItemType.TECHITEM, "nintendo ds lite", "new desc");
        service.update(updatedSpec);

        ItemSpec fetched = service.get(1L);
        assertEquals(updatedSpec.toEntity().toStringNoId(), fetched.toEntity().toStringNoId());
    }
}
