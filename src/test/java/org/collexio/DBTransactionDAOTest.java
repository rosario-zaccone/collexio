package org.collexio;

import org.collexio.persistence.dao.DBTransactionDAO;
import org.collexio.persistence.model.TransactionEntity;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBTransactionDAOTest {

    private Connection connection;
    private DBTransactionDAO transactionDAO;

    @BeforeAll
    void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (var stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("""
                CREATE TABLE items (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    quantity INTEGER NOT NULL,
                    description TEXT,
                    type INTEGER NOT NULL,
                    item_collection_id INTEGER
                );
            """);

            stmt.execute("""
                INSERT INTO items (name, quantity, description, type, item_collection_id)
                VALUES ('Test Item 1', 1, 'First test item', 0, NULL),
                       ('Test Item 2', 2, 'Second test item', 1, NULL);
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
        }
        transactionDAO = new DBTransactionDAO(connection);
    }

    @BeforeEach
    void clearData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_transactions;");
        }
    }

    @AfterAll
    void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void testAddAndGetTransaction() throws SQLException {
        TransactionEntity t = new TransactionEntity(100.0, true, LocalDate.now());
        transactionDAO.add(t, 1L);

        List<TransactionEntity> all = transactionDAO.getAll();
        TransactionEntity saved = all.get(0);

        assertEquals(t.toStringNoId(), saved.toStringNoId());
    }

    @Test
    void testGetAllTransactions() throws SQLException {
        TransactionEntity t1 = new TransactionEntity(50.0, false, LocalDate.now().minusDays(1));
        TransactionEntity t2 = new TransactionEntity(200.0, true, LocalDate.now());
        transactionDAO.add(t1, 1L);
        transactionDAO.add(t2, 1L);

        List<TransactionEntity> all = transactionDAO.getAll();
        assertEquals(2, all.size());
    }

    @Test
    void testGetByItemId() throws SQLException {
        TransactionEntity t1 = new TransactionEntity(10.0, true, LocalDate.now());
        TransactionEntity t2 = new TransactionEntity(20.0, false, LocalDate.now());
        transactionDAO.add(t1, 1L);
        transactionDAO.add(t2, 1L);

        Set<String> set = new HashSet<>(); set.add(t1.toStringNoId()); set.add(t2.toStringNoId());


        List<TransactionEntity> item1Tx = transactionDAO.getByItemId(1L);
        List<TransactionEntity> item2Tx = transactionDAO.getByItemId(2L);

        Set<String> fetchedSet = new HashSet<>(); fetchedSet.add(item1Tx.get(0).toStringNoId()); fetchedSet.add(item1Tx.get(1).toStringNoId());
        assertEquals(2, item1Tx.size());
        assertEquals(fetchedSet, set);

        assertEquals(0, item2Tx.size());
    }

    @Test
    void testUpdateTransaction() throws SQLException {
        TransactionEntity t = new TransactionEntity(75.0, false, LocalDate.now());
        transactionDAO.add(t, 1L);

        List<TransactionEntity> all = transactionDAO.getAll();
        TransactionEntity saved = all.get(0);

        TransactionEntity updated = new TransactionEntity(saved.getId(), 150.0, true, LocalDate.now().plusDays(1));
        transactionDAO.update(updated, false);

        Optional<TransactionEntity> fetched = transactionDAO.get(saved.getId());
        assertTrue(fetched.isPresent());
        assertEquals(150.0, fetched.get().getAmount());
        assertTrue(fetched.get().isIncome());
    }

    @Test
    void testDeleteTransaction() throws SQLException {
        TransactionEntity t = new TransactionEntity(30.0, false, LocalDate.now());
        transactionDAO.add(t, 1L);

        List<TransactionEntity> all = transactionDAO.getAll();
        TransactionEntity saved = all.get(0);

        transactionDAO.delete(saved.getId());

        Optional<TransactionEntity> fetched = transactionDAO.get(saved.getId());
        assertFalse(fetched.isPresent());
    }
}