package org.collexio;

import org.collexio.domain.*;
import org.collexio.persistence.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DBItemDAOTest {
    private Connection connection;
    private DBItemDAO itemDAO;

    @BeforeEach
    public void setupDatabase() throws SQLException, IOException {
        connection = ConnectionFactory.getConnection();
        itemDAO = new DBItemDAO(connection);
        DBItemCollectionDAO collectionDao = new DBItemCollectionDAO(connection);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_collections");
            stmt.execute("DELETE FROM item_transactions");
            stmt.execute("DELETE FROM item_photos");
            stmt.execute("DELETE FROM items");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name IN ('item_collections','item_photos','items','item_transactions')");
        }
        ItemCollection collection = new ItemCollection("books");
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
    public void testGetAdd() throws SQLException {
        ItemPhoto p1 = new ItemPhoto(Paths.get("plant.jpg"), LocalDate.now());
        ItemPhoto p2 = new ItemPhoto(Paths.get("book.jpg"), LocalDate.now());
        ItemPhoto p3 = new ItemPhoto(Paths.get("tech.jpg"), LocalDate.now());

        Plant plant = new Plant("Rose", 5, p1, "flower plant", "rosa_canina");
        Book book = new Book("Clean Code", 3, p2, "cs book");
        TechItem tech = new TechItem("Laptop", 2, p3, "laptop for studying");

        for (Item item : List.of(plant, book, tech)) {
            for (int i = 0; i < 4; i++) {
                item.addTransaction(new Transaction(50.0 + i * 10, i % 2 == 0, LocalDate.now()));
            }
            itemDAO.add(item, 1L);
        }

        List<Item> allItems = itemDAO.getAll();
        assertEquals(3, allItems.size());
        assertEquals(plant, allItems.get(0));
        assertEquals(book, allItems.get(1));
        assertEquals(tech, allItems.get(2));

        Optional<Item> retrieved = itemDAO.get(1L);
        assertEquals(retrieved.get(), plant);
    }


    @Test
    public void testUpdateItems() throws SQLException {
        ItemPhoto p1 = new ItemPhoto(Paths.get("plant.jpg"), LocalDate.now());
        Item plant = new Plant("Tulip", 5, p1, "flower plant", "tulipa_gesneriana" );
        itemDAO.add(plant, 1L);
        Long plantId = itemDAO.getAll().get(0).getId();

        ItemPhoto p2 = new ItemPhoto(Paths.get("book.jpg"), LocalDate.now());
        Item book = new Book("Clean Code", 3, p2, "cs book");
        itemDAO.add(book, 1L);
        Long bookId = itemDAO.getAll().get(1).getId();

        ItemPhoto p3 = new ItemPhoto(Paths.get("tech.jpg"), LocalDate.now());
        Item tech = new TechItem("Laptop", 2, p3, "laptop fr studying");
        itemDAO.add(tech, 1L);
        Long techId = itemDAO.getAll().get(2).getId();


        plant = new Plant(plantId, "Tulip Updated", 10, p1, "plant with red flowers", "tulipa_gesnerianaUPDATE");
        book = new Book(bookId, "Clean Code v2", 5, p2, "computer science book");
        tech = new TechItem(techId, "Laptop Pro", 3, p3, "laptop");

        itemDAO.update(plant, false);
        itemDAO.update(book, false);
        itemDAO.update(tech, false);

        assertEquals(plant, itemDAO.get(plantId).get());
        assertEquals(book, itemDAO.get(bookId).get());
        assertEquals(tech, itemDAO.get(techId).get());
    }


    @Test
    public void testDeleteItems() throws SQLException {
        ItemPhoto p1 = new ItemPhoto(Paths.get("plant.jpg"), LocalDate.now());
        Plant plant = new Plant("Cactus", 2, p1, "succulent plant", "opuntia_ficus");
        itemDAO.add(plant, 1L);
        Long plantId = itemDAO.getAll().get(0).getId();

        ItemPhoto p2 = new ItemPhoto(Paths.get("book.jpg"), LocalDate.now());
        Book book = new Book("Effective Java", 1, p2, "java book for pro");
        itemDAO.add(book, 1L);
        Long bookId = itemDAO.getAll().get(1).getId();

        itemDAO.delete(plantId);
        itemDAO.delete(bookId);

        assertFalse(itemDAO.get(plantId).isPresent());
        assertFalse(itemDAO.get(bookId).isPresent());
    }
}
