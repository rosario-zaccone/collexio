package org.collexio;

import org.collexio.persistence.dao.DBItemPhotoDAO;
import org.collexio.persistence.entity.ItemPhotoEntity;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBItemPhotoDAOTest {

    private Connection connection;
    private DBItemPhotoDAO dao;

    @BeforeAll
    void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
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
                        item_collection_id INTEGER
                    );
                    """);
            stmt.execute("""
                    CREATE TABLE item_photos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        path TEXT NOT NULL,
                        photo_date TEXT NOT NULL,
                        item_id INTEGER UNIQUE,
                        FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
                    );
                    """);
            stmt.execute("INSERT INTO item_specs (type, name, description) VALUES (1, 'DS', 'Desc');");
            stmt.execute("INSERT INTO items (status, item_spec_id, item_collection_id) VALUES (2, 1, NULL);");
            stmt.execute("INSERT INTO items (status, item_spec_id, item_collection_id) VALUES (2, 1, NULL);");
        }
        dao = new DBItemPhotoDAO(connection);
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
    void addAndGetById() throws SQLException {
        ItemPhotoEntity photo = new ItemPhotoEntity(Path.of("photo.png"), LocalDate.of(2024, 1, 1));

        ItemPhotoEntity saved = dao.add(photo, 1L);

        assertEquals(saved.toString(), dao.get(saved.getId()).orElseThrow().toString());
    }

    @Test
    void getByItemIdAndGetAll() throws SQLException {
        ItemPhotoEntity first = dao.add(new ItemPhotoEntity(Path.of("first.png"), LocalDate.of(2024, 1, 1)), 1L);
        dao.add(new ItemPhotoEntity(Path.of("second.png"), LocalDate.of(2024, 1, 2)), 2L);

        assertEquals(first.toString(), dao.getByItemId(1L).orElseThrow().toString());
        assertEquals(2, dao.getAll().size());
    }

    @Test
    void updatePhoto() throws SQLException {
        ItemPhotoEntity saved = dao.add(new ItemPhotoEntity(Path.of("old.png"), LocalDate.of(2024, 1, 1)), 1L);
        ItemPhotoEntity updated = new ItemPhotoEntity(saved.getId(), Path.of("new.png"), LocalDate.of(2024, 1, 2));

        dao.update(updated);

        assertEquals(updated.toString(), dao.get(saved.getId()).orElseThrow().toString());
    }

    @Test
    void deleteByItemId() throws SQLException {
        dao.add(new ItemPhotoEntity(Path.of("photo.png"), LocalDate.of(2024, 1, 1)), 1L);

        dao.deleteByItemId(1L);

        Optional<ItemPhotoEntity> deleted = dao.getByItemId(1L);
        assertTrue(deleted.isEmpty());
    }
}
