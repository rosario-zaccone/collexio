package org.collexio;

import com.google.api.client.util.ObjectParser;
import org.collexio.domain.ItemPhoto;
import org.collexio.persistence.ConnectionFactory;
import org.collexio.persistence.DBItemPhotoDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Optional<ItemPhoto> o1 = dao.get("P-1");
        Optional<ItemPhoto> o2 = dao.get("P-11111");
        System.out.println(dao.get("P-1"));
        assertTrue(o2.isEmpty());


    }

}
