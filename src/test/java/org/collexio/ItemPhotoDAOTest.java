package org.collexio;

import com.google.api.client.util.ObjectParser;
import org.collexio.domain.ItemPhoto;
import org.collexio.persistence.ConnectionFactory;
import org.collexio.persistence.DBItemPhotoDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ItemPhotoDAOTest {
    private static DBItemPhotoDAO dao;

    @BeforeAll
    public static void setUp() throws IOException, SQLException {
        dao = new DBItemPhotoDAO(ConnectionFactory.getConnection());
    }

    @Test
    public void getAllTest() throws SQLException {
        List<ItemPhoto> res = dao.getAll();
        res.forEach(System.out::println);
    }

    @Test
    public void getTest() throws SQLException {
        assertDoesNotThrow(() -> dao.get(1).orElseThrow(NoSuchElementException::new));
        assertThrows(NoSuchElementException.class, () -> dao.get(1111).orElseThrow(NoSuchElementException::new));
    }

    @Disabled
    @Test
    public void insertTest() throws SQLException {
        ItemPhoto photo = new ItemPhoto(Paths.get("/data/photos/test.png"), LocalDateTime.now());
        dao.add(photo, 10);
        assertDoesNotThrow(() -> dao.get(10).orElseThrow(NoSuchElementException::new));
    }

}
