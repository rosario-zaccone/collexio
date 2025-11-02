package org.collexio;


import org.collexio.domain.*;
import org.collexio.persistence.ConnectionFactory;
import org.collexio.persistence.DBItemCollectionDAO;
import org.collexio.persistence.DBItemPhotoDAO;
import org.collexio.persistence.DBTransactionDAO;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DBTransactionDAOTest {
    private Connection connection;
    private DBTransactionDAO dao;

    @BeforeEach
    public void setupDatabase() throws SQLException, IOException {
        connection = ConnectionFactory.getConnection();
        dao = new DBTransactionDAO(connection);
        DBItemCollectionDAO<Book> collectionDao = new DBItemCollectionDAO<>(connection);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_collections");
            stmt.execute("DELETE FROM item_transactions");
            stmt.execute("DELETE FROM item_photos");
            stmt.execute("DELETE FROM items");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name IN ('item_collections','item_photos','items','item_transactions')");
        }
        ItemPhoto p = new ItemPhoto(Paths.get("book.jpg"), LocalDateTime.now());
        Item book = new Book("Clean Code", 3, p, "cs book");
        for (int i = 0; i < 4; i++) {
            book.addTransaction(new Transaction(50.0 + i * 10, i % 2 == 0, LocalDateTime.now()));
        }
        ItemCollection<Book> collection = new ItemCollection<>("books");
        collection.addItem((Book) book);
        collectionDao.add(collection);
    }

    @AfterEach
    public void teardownDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_collections");
            stmt.execute("DELETE FROM item_transactions");
            stmt.execute("DELETE FROM item_photos");
            stmt.execute("DELETE FROM items");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name IN ('item_collections','item_photos','items','item_transactions')");
        }
        connection.close();
    }


    @Test
    public void testAddGetTransaction() throws SQLException {
        Transaction transaction = new Transaction(100.0, true, LocalDateTime.now());
        dao.add(transaction, 1L);
        Optional<Transaction> result = dao.get(1L);
        assertTrue(result.isPresent());
        assertEquals(transaction, result.get());
    }


    @Test
    public void testUpdateTransaction() throws SQLException {
        Transaction transaction = new Transaction(200.0, true, LocalDateTime.now());
        dao.add(transaction, 1L);
        transaction = new Transaction(1L, 300.0, false, LocalDateTime.now());
        dao.update(transaction, false);
        Optional<Transaction> result = dao.get(1L);
        assertEquals(result.get(), transaction);
    }

    @Test
    public void testDeleteTransaction() throws SQLException {
        Transaction transaction = new Transaction(150.0, true, LocalDateTime.now());
        dao.add(transaction, 1L);
        dao.delete(1L);
        Optional<Transaction> result = dao.get(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAllTransactions() throws SQLException {
        Transaction t1 = new Transaction(10.0, true, LocalDateTime.now());
        Transaction t2 = new Transaction(20.0, false, LocalDateTime.now());
        dao.add(t1, 1L);
        dao.add(t2, 1L);
        List<Transaction> allTransactions = dao.getAll();
        assertNotNull(allTransactions);
        assertEquals(2, allTransactions.size());
        assertEquals(allTransactions.get(0), t1);
        assertEquals(allTransactions.get(1), t2);
    }
}

