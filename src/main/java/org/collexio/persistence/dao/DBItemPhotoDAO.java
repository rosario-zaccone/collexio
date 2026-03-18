package org.collexio.persistence.dao;

import org.collexio.persistence.model.ItemPhotoEntity;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBItemPhotoDAO implements ItemPhotoDAO {
    private final Connection connection;

    private static final String selectAllSql = "SELECT * FROM item_photos";
    private static final String selectSql = "SELECT * FROM item_photos WHERE id=?";
    private static final String selectMaxIdSql =
            "SELECT * FROM item_photos ORDER BY id DESC LIMIT 1";
    private static final String selectByItemIdSql = "SELECT * FROM item_photos WHERE item_id=?";
    private static final String insertSql = "INSERT INTO item_photos(path, photo_date, item_id) VALUES (?,?,?)";
    private static final String updateSql = "UPDATE item_photos SET path = ? , "
            + "photo_date = ? "
            + "WHERE id = ?";

    public DBItemPhotoDAO(Connection connection) {
        this.connection = connection; // DI
    }



    @Override
    public Optional<ItemPhotoEntity> get(Long id) throws SQLException {
        Optional<ItemPhotoEntity> res = Optional.empty();
        try (var stmt = connection.prepareStatement(selectSql)) {
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                Path path = Paths.get(rs.getString("path"));
                LocalDate date = LocalDate.parse(rs.getString("photo_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                res = Optional.of(new ItemPhotoEntity(id, path, date));
            }
            rs.close();
        }
        return res;
    }

    @Override
    public Optional<ItemPhotoEntity> getByItemId(Long itemId) throws SQLException {
        Optional<ItemPhotoEntity> res = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(selectByItemIdSql)) {
            stmt.setLong(1, itemId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    Path path = Paths.get(rs.getString("path"));
                    LocalDate date = LocalDate.parse(rs.getString("photo_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                    res = Optional.of(new ItemPhotoEntity(id, path, date));
                }
            }
        }
        return res;
    }

    @Override
    public void update(ItemPhotoEntity photo) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setLong(3, photo.getId());
            stmt.setString(1, photo.getPath().toString());
            stmt.setString(2, photo.getDate().toString());
            stmt.executeUpdate();
        }
    }


    @Override
    public List<ItemPhotoEntity> getAll() throws SQLException {
        List<ItemPhotoEntity> res = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllSql)) {
            while (rs.next()) {
                long id = rs.getLong("id");
                Path path = Paths.get(rs.getString("path"));
                LocalDate date = LocalDate.parse(rs.getString("photo_date"));
                res.add(new ItemPhotoEntity(id, path, date));
            }
        }
        return res;
    }

    @Override
    public Optional<ItemPhotoEntity> getLast() throws SQLException {
        Optional<ItemPhotoEntity> res = Optional.empty();
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectMaxIdSql)){
                while (rs.next()) {
                    Path path = Paths.get(rs.getString("path"));
                    LocalDate date = LocalDate.parse(rs.getString("photo_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                    res = Optional.of(new ItemPhotoEntity(rs.getLong("id"), path, date));
                }
            }
        return res;
    }

    @Override
    public void add(ItemPhotoEntity photo, Long itemId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
            stmt.setString(1, photo.getPath().toString());
            stmt.setString(2, photo.getDate().toString());
            stmt.setLong(3, itemId);
            stmt.executeUpdate();
        }
    }
}
