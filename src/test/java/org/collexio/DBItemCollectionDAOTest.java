package org.collexio;

import org.collexio.domain.*;
import org.collexio.persistence.ConnectionFactory;
import org.collexio.persistence.DBItemCollectionDAO;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DBItemCollectionDAOTest {

    private Connection connection;
    private DBItemCollectionDAO collectionDAO;

    @BeforeEach
    public void setup() throws SQLException, IOException {
        connection = ConnectionFactory.getConnection();
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_collections");
            stmt.execute("DELETE FROM item_photos");
            stmt.execute("DELETE FROM items");
            stmt.execute("DELETE FROM item_transactions");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name IN ('item_collections','item_photos','items','item_transactions')");
        }
        collectionDAO = new DBItemCollectionDAO(connection);
    }

    @AfterEach
    public void cleanup() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_collections");
            stmt.execute("DELETE FROM item_photos");
            stmt.execute("DELETE FROM items");
            stmt.execute("DELETE FROM item_transactions");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name IN ('item_collections','item_photos','items','item_transactions')");
        }
        connection.close();
    }

    @Test
    public void testCrud() throws SQLException {
        ItemCollection collection = new ItemCollection("pokemon games");

        ItemPhoto redPhoto = new ItemPhoto(Paths.get("pokemon_red.jpg"), LocalDate.now());
        TechItem red = new TechItem("Pokemon Red", 10, redPhoto, "pokemon game 1 gen");
        for (int i = 0; i < 4; i++)
            red.addTransaction(new Transaction(100 + i * 10, i % 2 == 0, LocalDate.now()));

        ItemPhoto bluePhoto = new ItemPhoto(Paths.get("pokemon_blue.jpg"), LocalDate.now());
        TechItem blue = new TechItem("Pokemon Blue", 12, bluePhoto, "pokemon game 1 gen");
        for (int i = 0; i < 4; i++)
            blue.addTransaction(new Transaction(200 + i * 10, i % 2 == 0, LocalDate.now()));

        ItemPhoto greenPhoto = new ItemPhoto(Paths.get("pokemon_green.jpg"), LocalDate.now());
        TechItem green = new TechItem("Pokemon Green", 8, greenPhoto, "pokemon game 1 gen");
        for (int i = 0; i < 4; i++)
            green.addTransaction(new Transaction(150 + i * 10, i % 2 == 0, LocalDate.now()));

        ItemPhoto goldPhoto = new ItemPhoto(Paths.get("pokemon_gold.jpg"), LocalDate.now());
        TechItem gold = new TechItem("Pokemon Gold", 15, goldPhoto, "pokemon game 1 gen");
        for (int i = 0; i < 4; i++)
            gold.addTransaction(new Transaction(300 + i * 10, i % 2 == 0, LocalDate.now()));

        collection.addItem(red);
        collection.addItem(blue);
        collection.addItem(green);
        collection.addItem(gold);
        collectionDAO.add(collection);

        ItemCollection retrieved = collectionDAO.get(1L).get();
        ItemCollection zeldaCollection = new ItemCollection("zelda games");

        ItemPhoto ootPhoto = new ItemPhoto(Paths.get("zelda_ocarina.jpg"), LocalDate.now());
        TechItem ocarina = new TechItem("Zelda Ocarina of Time", 20, ootPhoto, "zelda game");
        for (int i = 0; i < 4; i++)
            ocarina.addTransaction(new Transaction(400 + i * 20, i % 2 == 0, LocalDate.now()));

        ItemPhoto mmPhoto = new ItemPhoto(Paths.get("zelda_majoras.jpg"), LocalDate.now());
        TechItem majoras = new TechItem("Zelda Majora’s Mask", 18, mmPhoto, "zelda game");
        for (int i = 0; i < 4; i++)
            majoras.addTransaction(new Transaction(350 + i * 20, i % 2 == 0, LocalDate.now()));

        ItemPhoto wwPhoto = new ItemPhoto(Paths.get("zelda_windwaker.jpg"), LocalDate.now());
        TechItem windWaker = new TechItem("Zelda Wind Waker", 22, wwPhoto, "zelda game");
        for (int i = 0; i < 4; i++)
            windWaker.addTransaction(new Transaction(500 + i * 15, i % 2 == 0, LocalDate.now()));

        ItemPhoto botwPhoto = new ItemPhoto(Paths.get("zelda_botw.jpg"), LocalDate.now());
        TechItem botw = new TechItem("Zelda Breath of the Wild", 30, botwPhoto, "zelda game");
        for (int i = 0; i < 4; i++)
            botw.addTransaction(new Transaction(700 + i * 25, i % 2 == 0, LocalDate.now()));

        zeldaCollection.addItem(ocarina);
        zeldaCollection.addItem(majoras);
        zeldaCollection.addItem(windWaker);
        zeldaCollection.addItem(botw);

        collectionDAO.add(zeldaCollection);

        ItemCollection retrievedZelda = collectionDAO.get(2L).get();
        assertEquals(retrievedZelda.toStringNoId(), zeldaCollection.toStringNoId());
        assertEquals(retrieved.toStringNoId(), collection.toStringNoId());
        assertEquals(retrieved, collection);
        assertEquals(retrievedZelda, zeldaCollection);

    }

}
