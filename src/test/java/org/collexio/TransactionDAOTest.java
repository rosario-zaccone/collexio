package org.collexio;


import org.collexio.domain.Transaction;
import org.collexio.persistence.ConnectionFactory;
import org.collexio.persistence.DBTransactionDAO;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionDAOTest {

    private static Connection connection;
    private DBTransactionDAO transactionDAO;

    @BeforeAll
    public static void setupDatabase() throws SQLException, IOException {
        connection = ConnectionFactory.getConnection();
    }
    @AfterAll
    public static void teardownDatabase() throws SQLException {
        connection.close();
    }

    @BeforeEach
    public void setup() {
        transactionDAO = new DBTransactionDAO(connection);
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_transactions");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_transactions'");
        }
    }

    @Test
    public void testAddTransaction() throws SQLException {
        Transaction transaction = new Transaction(100.0, true, LocalDateTime.now());
        transactionDAO.add(transaction, 1);
        Optional<Transaction> result = transactionDAO.get(1);
        assertTrue(result.isPresent());
        assertEquals(100.0, result.get().getAmount());
        assertTrue(result.get().isIncome());
    }

    @Test
    public void testGetTransactionById() throws SQLException {
        Transaction transaction = new Transaction(50.0, false, LocalDateTime.now());
        transactionDAO.add(transaction, 1);
        Optional<Transaction> result = transactionDAO.get(1);
        assertEquals(1, result.get().getId());
        assertEquals(50.0, result.get().getAmount());
    }

    @Test
    public void testUpdateTransaction() throws SQLException {
        Transaction transaction = new Transaction(200.0, true, LocalDateTime.now());
        transactionDAO.add(transaction, 1);
        transaction = new Transaction(1, 300.0, false, LocalDateTime.now());
        transactionDAO.update(transaction, false);
        Optional<Transaction> result = transactionDAO.get(1);
        assertTrue(result.isPresent());
        assertEquals(300.0, result.get().getAmount());
        assertFalse(result.get().isIncome());
    }

    @Test
    public void testDeleteTransaction() throws SQLException {
        Transaction transaction = new Transaction(150.0, true, LocalDateTime.now());
        transactionDAO.add(transaction, 1);
        transactionDAO.delete(1);
        Optional<Transaction> result = transactionDAO.get(1);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAllTransactions() throws SQLException {
        Transaction t1 = new Transaction(10.0, true, LocalDateTime.now());
        Transaction t2 = new Transaction(20.0, false, LocalDateTime.now());
        transactionDAO.add(t1, 1);
        transactionDAO.add(t2, 1);
        List<Transaction> allTransactions = transactionDAO.getAll();
        assertNotNull(allTransactions);
        assertEquals(2, allTransactions.size());
    }
}

