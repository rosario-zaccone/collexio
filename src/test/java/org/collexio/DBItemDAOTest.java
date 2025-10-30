package org.collexio;

import org.collexio.domain.*;
import org.collexio.persistence.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DBItemDAOTest {

    private static Connection connection;
    private DBItemDAO itemDAO;

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
        itemDAO = new DBItemDAO(connection);
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_transactions"); // pulizia transazioni
            stmt.execute("DELETE FROM item_photos");
            stmt.execute("DELETE FROM items");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='items'");
        }
    }

    // ----------------------------
    // ADD ITEMS WITH TRANSACTIONS
    // ----------------------------
    @Test
    public void testAddItemsWithTransactions() throws SQLException {
        ItemPhoto p1 = new ItemPhoto(Paths.get("plant.jpg"), LocalDateTime.now());
        ItemPhoto p2 = new ItemPhoto(Paths.get("book.jpg"), LocalDateTime.now());
        ItemPhoto p3 = new ItemPhoto(Paths.get("tech.jpg"), LocalDateTime.now());

        Plant plant = new Plant("Rose", 5, p1, "rosa_canina");
        Book book = new Book("Clean Code", 3, p2);
        TechItem tech = new TechItem("Laptop", 2, p3);

        for (Item item : List.of(plant, book, tech)) {
            for (int i = 0; i < 4; i++) {
                item.addTransaction(new Transaction(50.0 + i * 10, i % 2 == 0, LocalDateTime.now()));
            }
            itemDAO.add(item, 1);
        }

        List<Item> allItems = itemDAO.getAll();
        assertEquals(3, allItems.size());

        // Verifica che ogni item abbia 4 transazioni
        for (Item item : allItems) {
            assertEquals(4, item.getTransactions().size());
            System.out.println(item);
        }
    }

    // ----------------------------
    // UPDATE ITEMS
    // ----------------------------
    @Test
    public void testUpdateItems() throws SQLException {
        // Aggiungi tutti e tre
        ItemPhoto p1 = new ItemPhoto(Paths.get("plant.jpg"), LocalDateTime.now());
        Plant plant = new Plant("Tulip", 5, p1, "tulipa_gesneriana");
        itemDAO.add(plant, 1);
        int plantId = itemDAO.getAll().get(0).getId();

        ItemPhoto p2 = new ItemPhoto(Paths.get("book.jpg"), LocalDateTime.now());
        Book book = new Book("Clean Code", 3, p2);
        itemDAO.add(book, 1);
        int bookId = itemDAO.getAll().get(1).getId();

        ItemPhoto p3 = new ItemPhoto(Paths.get("tech.jpg"), LocalDateTime.now());
        TechItem tech = new TechItem("Laptop", 2, p3);
        itemDAO.add(tech, 1);
        int techId = itemDAO.getAll().get(2).getId();

        // Update dati
        plant = new Plant(plantId, "Tulip Updated", 10, p1, "tulipa_gesneriana");
        book = new Book(bookId, "Clean Code v2", 5, p2);
        tech = new TechItem(techId, "Laptop Pro", 3, p3);

        itemDAO.update(plant, false);
        itemDAO.update(book, false);
        itemDAO.update(tech, false);

        assertEquals("Tulip Updated", itemDAO.get(plantId).get().getName());
        assertEquals("Clean Code v2", itemDAO.get(bookId).get().getName());
        assertEquals("Laptop Pro", itemDAO.get(techId).get().getName());
    }

    // ----------------------------
    // DELETE ITEMS
    // ----------------------------
    @Test
    public void testDeleteItems() throws SQLException {
        ItemPhoto p1 = new ItemPhoto(Paths.get("plant.jpg"), LocalDateTime.now());
        Plant plant = new Plant("Cactus", 2, p1, "opuntia_ficus");
        itemDAO.add(plant, 1);
        int plantId = itemDAO.getAll().get(0).getId();

        ItemPhoto p2 = new ItemPhoto(Paths.get("book.jpg"), LocalDateTime.now());
        Book book = new Book("Effective Java", 1, p2);
        itemDAO.add(book, 1);
        int bookId = itemDAO.getAll().get(1).getId();

        itemDAO.delete(plantId);
        itemDAO.delete(bookId);

        assertFalse(itemDAO.get(plantId).isPresent());
        assertFalse(itemDAO.get(bookId).isPresent());
    }
}
