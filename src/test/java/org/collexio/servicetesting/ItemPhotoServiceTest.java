package org.collexio.servicetesting;

import org.collexio.business.domain.ItemPhoto;
import org.collexio.business.service.ItemPhotoService;
import org.collexio.persistence.dao.DBItemPhotoDAO;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemPhotoServiceTest {

    private Connection connection;
    private ItemPhotoService service;

    @BeforeAll
    void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");

            // Collections
            stmt.execute("""
                        CREATE TABLE item_collections (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE item_specs(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            type INTEGER NOT NULL, -- 0 for plant, 1 for tech, 2 for book
                            name TEXT NOT NULL,
                            description TEXT NOT NULL
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE items(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            status INTEGER NOT NULL, -- 0 for bad 1 for average 2 for good
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
                            item_id INTEGER UNIQUE ,
                            FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE ON UPDATE CASCADE
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE item_transactions (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            amount REAL NOT NULL,
                            income INTEGER NOT NULL, -- boolean
                            transaction_date TEXT NOT NULL,
                            item_id INTEGER,
                            FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE ON UPDATE CASCADE
                        );
                    """);

            stmt.execute("""
                        INSERT INTO item_collections (name)
                        VALUES ('Console');
                    """);

            stmt.execute("""
                        INSERT INTO item_specs (type, name, description)
                        VALUES (1, 'Nintendo', 'Console Nintendo');
                    """);

            stmt.execute("""
                        INSERT INTO items (status, item_spec_id, item_collection_id)
                        VALUES 
                            (2, 1, 1),  -- primo item, GOOD
                            (1, 1, 1);  -- secondo item, AVERAGE
                    """);
        }

        service = new ItemPhotoService(new DBItemPhotoDAO(connection));
    }

    @BeforeEach
    void clearData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM item_photos;");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='item_photos';");

        }
    }

    @AfterAll
    void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void testAddGet() throws SQLException, IOException {
        ItemPhoto photo = new ItemPhoto( Path.of("images/test/test.png"), LocalDate.MIN);
        photo = service.add(photo, 1L);
        ItemPhoto fetched = service.getByItemId(1L);
        assertEquals(photo.toString(), fetched.toString());
    }

    @Test
    void testUpdate() throws SQLException, IOException {
        ItemPhoto photo = new ItemPhoto(Path.of("images/test/test.png"), LocalDate.MIN);
        photo = service.add(photo, 1L);
        ItemPhoto n = service.update(photo, 1L);
        assertEquals(service.getByItemId(1L).toString(), n.toString());
    }

    @Test
    void testDeletePhoto() throws SQLException, IOException {
        ItemPhoto photo = new ItemPhoto(null, Path.of("images/test/test.png"));
        ItemPhoto saved = service.add(photo, 1L);
        service.deleteByItemId(1L);
        assertThrows(NoSuchElementException.class, () -> {
            service.getByItemId(1L);
        });
    }

}