package org.collexio;
import org.collexio.persistence.dao.DBTransactionDAO;
import org.collexio.persistence.model.TransactionEntity;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

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
            CREATE TABLE item_collections(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL
            );
        """);

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
                FOREIGN KEY(item_spec_id) REFERENCES item_specs(id) ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY(item_collection_id) REFERENCES item_collections(id) ON DELETE SET NULL ON UPDATE CASCADE
            );
        """);

            stmt.execute("""
            CREATE TABLE item_photos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                path TEXT NOT NULL,
                photo_date TEXT NOT NULL,
                item_id INTEGER UNIQUE,
                FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE ON UPDATE CASCADE
            );
        """);
            stmt.execute("""
            CREATE TABLE item_transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                amount REAL NOT NULL,
                income INTEGER NOT NULL,
                transaction_date TEXT NOT NULL,
                item_id INTEGER,
                FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE ON UPDATE CASCADE
            );
        """);

            stmt.execute("""
            INSERT INTO item_specs (type, name, description)
            VALUES 
                (0, 'Plant Spec', 'A plant'),
                (1, 'Tech Spec', 'A tech item');
        """);

            stmt.execute("""
            INSERT INTO item_collections (name)
            VALUES 
                ('Collection A'),
                ('Collection B');
        """);

            stmt.execute("""
            INSERT INTO items (status, item_spec_id, item_collection_id)
            VALUES 
                (2, 1, 1),
                (1, 2, NULL);
        """);

            stmt.execute("""
            INSERT INTO item_transactions (amount, income, transaction_date, item_id)
            VALUES 
                (10.5, 1, '2024-01-01', 1),
                (5.0, 0, '2024-01-02', 2);
        """);
        }

        transactionDAO = new DBTransactionDAO(connection);
    }

    @BeforeEach
    void clearData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_transactions;");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_transactions';");
        }
    }

    @AfterAll
    void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void testAddGetTransaction() throws SQLException {
        TransactionEntity t = new TransactionEntity(100.0, true, LocalDate.now());
        TransactionEntity t2 = new TransactionEntity(1000.0, true, LocalDate.now());
        t = transactionDAO.add(t, 1L);
        t2 = transactionDAO.add(t2, 1L);

        TransactionEntity fetched = transactionDAO.get(t.getId()).get();
        assertEquals(t.toStringNoId(), fetched.toStringNoId());

        Optional<TransactionEntity> empty = transactionDAO.get(4L);
        assertTrue(empty.isEmpty());
    }

    @Test
    void testGetAllTransactions() throws SQLException {
        TransactionEntity t1 = new TransactionEntity(50.0, false, LocalDate.now().minusDays(1));
        TransactionEntity t2 = new TransactionEntity(200.0, true, LocalDate.now());
        Set<String> set = new HashSet<>(); set.add(t1.toStringNoId()); set.add(t2.toStringNoId());
        transactionDAO.add(t1, 1L);
        transactionDAO.add(t2, 1L);


        List<TransactionEntity> all = transactionDAO.getAll();
        Set<String> fetched = new HashSet<>(); fetched.add(all.get(0).toStringNoId()); fetched.add(all.get(1).toStringNoId());
        assertEquals(2, all.size());
        assertEquals(fetched, set);

    }

    @Test
    void testGetByItemId() throws SQLException {
        TransactionEntity t1 = new TransactionEntity(10.0, true, LocalDate.now());
        TransactionEntity t2 = new TransactionEntity(20.0, false, LocalDate.now());
        TransactionEntity t3 = new TransactionEntity(20.0, false, LocalDate.now());
        transactionDAO.add(t1, 1L);
        transactionDAO.add(t2, 1L);
        transactionDAO.add(t3, 2L);

        Set<String> set = new HashSet<>(); set.add(t1.toStringNoId()); set.add(t2.toStringNoId());


        List<TransactionEntity> item1Tx = transactionDAO.getByItemId(1L);
        List<TransactionEntity> item2Tx = transactionDAO.getByItemId(2L);

        Set<String> fetchedSet = new HashSet<>(); fetchedSet.add(item1Tx.get(0).toStringNoId()); fetchedSet.add(item1Tx.get(1).toStringNoId());
        assertEquals(2, item1Tx.size());
        assertEquals(fetchedSet, set);

        assertEquals(1, item2Tx.size());
    }

    @Test
    void testUpdateTransaction() throws SQLException {
        TransactionEntity t = new TransactionEntity(75.0, false, LocalDate.now());
        transactionDAO.add(t, 1L);

        List<TransactionEntity> all = transactionDAO.getAll();
        TransactionEntity saved = all.get(0);

        TransactionEntity updated = new TransactionEntity(saved.getId(), 150.0, true, LocalDate.now().plusDays(1));
        transactionDAO.update(updated);

        Optional<TransactionEntity> fetched = transactionDAO.get(saved.getId());
        assertTrue(fetched.isPresent());
        assertEquals(updated.toStringNoId(), fetched.get().toStringNoId());
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