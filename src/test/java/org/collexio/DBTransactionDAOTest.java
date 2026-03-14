package org.collexio;


import org.collexio.business.domain.*;
import org.collexio.persistence.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DBTransactionDAOTest {
    private Connection connection;
    private DBTransactionDAO dao;

    @BeforeEach
    public void setupDatabase() throws SQLException, IOException {
        connection = ConnectionFactory.getConnection();
        dao = new DBTransactionDAO(connection);
        DBItemCollectionDAO collectionDao = new DBItemCollectionDAO(connection, new DBItemDAO(connection, new DBItemPhotoDAO(connection), new DBTransactionDAO(connection)));
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_collections");
            stmt.execute("DELETE FROM item_transactions");
            stmt.execute("DELETE FROM item_photos");
            stmt.execute("DELETE FROM items");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name IN ('item_collections','item_photos','items','item_transactions')");
        }
        ItemPhoto p = new ItemPhoto(Paths.get("book.jpg"), LocalDate.now());
        Item book = new Book("Clean Code", 3, p, "cs book");
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
        Transaction transaction = new Transaction(1L, 100.0, true, LocalDate.now());
        dao.add(transaction, 1L);
        Optional<Transaction> result = dao.get(1L);
        assertTrue(result.isPresent());
        assertEquals(transaction, result.get());
    }


    @Test
    public void testUpdateTransaction() throws SQLException {
        Transaction transaction = new Transaction(200.0, true, LocalDate.now());
        dao.add(transaction, 1L);
        transaction = new Transaction(1L, 300.0, false, LocalDate.now());
        dao.update(transaction, false);
        Optional<Transaction> result = dao.get(1L);
        assertEquals(result.get(), transaction);
    }

    @Test
    public void testDeleteTransaction() throws SQLException {
        Transaction transaction = new Transaction(150.0, true, LocalDate.now());
        dao.add(transaction, 1L);
        dao.delete(1L);
        Optional<Transaction> result = dao.get(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAllTransactions() throws SQLException {
        Transaction t1 = new Transaction(10.0, true, LocalDate.now());
        Transaction t2 = new Transaction(20.0, false, LocalDate.now());
        dao.add(t1, 1L);
        dao.add(t2, 1L);
        Set<Transaction> allTransactions = dao.getAll();
        assertNotNull(allTransactions);
        assertEquals(2, allTransactions.size());
    }
}

