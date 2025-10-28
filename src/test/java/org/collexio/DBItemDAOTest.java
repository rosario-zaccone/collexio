package org.collexio;

import org.collexio.domain.Item;
import org.collexio.domain.ItemPhoto;
import org.collexio.domain.Plant;
import org.collexio.domain.Transaction;
import org.collexio.persistence.ConnectionFactory;
import org.collexio.persistence.DBItemDAO;
import org.collexio.persistence.DBTransactionDAO;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DBItemDAOTest {

    private static Connection connection;
    private static DBItemDAO dao;

    @BeforeAll
    public static void setupDatabase() throws SQLException, IOException {
        connection = ConnectionFactory.getConnection();
        dao = new DBItemDAO(connection);
    }
    @AfterAll
    public static void teardownDatabase() throws SQLException {
        connection.close();
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM items");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='items'");
        }
    }

    @Test
    public void testAddItem() throws SQLException {
        Item item = new Plant("Primula", 2, new ItemPhoto(Paths.get("data/prova.png"), LocalDateTime.now()), "primula_vulgaris");

        dao.add(item, 6);
    }
}
