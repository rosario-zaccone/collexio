package org.collexio.servicetesting;

import org.collexio.business.domain.ItemSpec;
import org.collexio.business.service.ItemSpecService;
import org.collexio.persistence.dao.DBItemSpecDAO;
import org.collexio.persistence.entity.ItemType;
import org.junit.jupiter.api.*;

import java.io.IOException;
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
        spec = service.add(spec);
        ItemSpec fetched = service.get(spec.getId());
        assertEquals(spec.toEntity().toString(), fetched.toEntity().toString());

        assertThrows(NoSuchElementException.class, () -> service.get(2L));
    }

    @Test
    public void testDelete() throws SQLException {
        ItemSpec spec = new ItemSpec(ItemType.TECHITEM, "nintendo ds", "desc");
        spec = service.add(spec);
        service.delete(spec.getId());

        assertThrows(NoSuchElementException.class, () -> service.get(1L));
    }

    @Test
    public void testUpdate() throws SQLException {
        ItemSpec spec = new ItemSpec(ItemType.TECHITEM, "nintendo ds", "desc");
        spec = service.add(spec);

        ItemSpec updatedSpec = new ItemSpec(spec.getId(), ItemType.TECHITEM, "nintendo ds lite", "new desc");
        service.update(updatedSpec);

        ItemSpec fetched = service.get(1L);
        assertEquals(updatedSpec.toEntity().toString(), fetched.toEntity().toString());
    }

    @Test
    public void testPrice() throws SQLException {
        ItemSpec tech = new ItemSpec(ItemType.TECHITEM, "nintendo switch 2", "desc");
        //System.out.println(service.price(tech));

        //ItemSpec book = new ItemSpec(ItemType.BOOK, "Dragon Ball Super. Vol. 4", "desc");
        //System.out.println(service.price(book));
    }

    @Disabled
    @Test
    public void testGenerator() throws SQLException, IOException, InterruptedException {
        /*
        ItemSpec tech = new ItemSpec(ItemType.TECHITEM, "nintendo switch 2", "desc");
        System.out.println(service.generateDescriptionByAI(tech));

        ItemSpec book = new ItemSpec(ItemType.BOOK, "Dragon Ball Super. Vol. 4", "desc");
        System.out.println(service.generateDescriptionByAI(book));

        ItemSpec plant = new ItemSpec(ItemType.PLANT, "crassula ovata", "desc");
        System.out.println(service.generateDescriptionByAI(book));
        */
    }
}
