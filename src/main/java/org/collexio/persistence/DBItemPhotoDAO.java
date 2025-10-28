package org.collexio.persistence;

import org.collexio.domain.Item;
import org.collexio.domain.ItemPhoto;
import org.collexio.utilities.Utilities;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DBItemPhotoDAO implements ItemPhotoDAO {
    private final Connection connection;

    private static final String selectAllSql = "SELECT * FROM item_photos";
    private static final String selectSql = "SELECT * FROM item_photos WHERE id=?";
    private static final String insertSql = "INSERT INTO item_photos(path, photo_date, item_id) VALUES (?,?,?)";
    private static final String updateSql = "UPDATE item_photos SET path = ? , "
            + "photo_date = ? ,"
            + "WHERE id = ?";

    public DBItemPhotoDAO(Connection connection) {
        this.connection = connection; // DI
    }

    @Override
    public Optional<ItemPhoto> get(int id) throws SQLException {
        Optional<ItemPhoto> res = Optional.empty();
        if (id <= 0)
            throw new IllegalArgumentException("Invalid id");
        try (var stmt = connection.prepareStatement(selectSql)) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                Path path = Paths.get(rs.getString("path"));
                LocalDateTime date = LocalDateTime.parse(rs.getString("photo_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                res = Optional.of(new ItemPhoto(id, path, date));
            }
            rs.close();
        }
        return res;
    }

    @Override
    public void update(ItemPhoto photo) throws SQLException {
        try (var stmt = connection.prepareStatement(updateSql)) {
            stmt.setInt(3, photo.getId());
            stmt.setString(1, photo.getPath().toString());
            stmt.setString(2, photo.getTimestamp().toString());
            stmt.executeUpdate();
        }
    }


    @Override
    public List<ItemPhoto> getAll() throws SQLException {
        List<ItemPhoto> res = new ArrayList<>();
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(selectAllSql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                Path path = Paths.get(rs.getString("path"));
                LocalDateTime date = LocalDateTime.parse(rs.getString("photo_date"));
                res.add(new ItemPhoto(id, path, date));
            }
        }
        return res;
    }

    @Override
    public void add(ItemPhoto photo, int itemId) throws SQLException {
        // add control for see if there is just a photo with itemID (ItemId VALIDATION) TODO
        try (var stmt = connection.prepareStatement(insertSql)) {
            stmt.setString(1, photo.getPath().toString());
            stmt.setString(2, photo.getTimestamp().toString());
            stmt.setInt(3, itemId);
            stmt.executeUpdate();
        }
    }
}
