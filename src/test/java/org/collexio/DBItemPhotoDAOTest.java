package org.collexio;

import org.collexio.domain.*;
import org.collexio.persistence.ConnectionFactory;
import org.collexio.persistence.DBItemCollectionDAO;
import org.collexio.persistence.DBItemDAO;
import org.collexio.persistence.DBItemPhotoDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DBItemPhotoDAOTest {

    private Connection connection;
    private DBItemPhotoDAO dao;
    private Item book;

    @BeforeEach
    public void setupDatabase() throws SQLException, IOException {
        connection = ConnectionFactory.getConnection();
        dao = new DBItemPhotoDAO(connection);
        DBItemCollectionDAO collectionDao = new DBItemCollectionDAO(connection);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_transactions");
            stmt.execute("DELETE FROM item_photos");
            stmt.execute("DELETE FROM items");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name IN ('item_collections','item_photos','items','transactions')");
        }
        ItemPhoto p = new ItemPhoto(Paths.get("book.jpg"), LocalDate.now());
        book = new Book("Clean Code", 3, p, "cs book");
        for (int i = 0; i < 4; i++) {
            book.addTransaction(new Transaction(50.0 + i * 10, i % 2 == 0, LocalDate.now()));
        }
        ItemCollection collection = new ItemCollection("books");
        collection.addItem(book);
        collectionDao.add(collection);
    }

    @AfterEach
    public void teardownDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_transactions");
            stmt.execute("DELETE FROM item_photos");
            stmt.execute("DELETE FROM items");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='items'");
        }
        connection.close();
    }

    @Test
    public void testGet() throws SQLException {
        Optional<ItemPhoto> result = dao.get(1L);
        assertTrue(result.isPresent());
        assertEquals(result.get(), book.getPhoto());
    }

    @Test
    public void testGetNonExistent() {
        assertThrows(NoSuchElementException.class, () -> dao.get(999L).orElseThrow(NoSuchElementException::new));
    }
}
