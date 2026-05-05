package org.collexio.persistence.dao;
import org.collexio.persistence.entity.ItemPhotoEntity;

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
    private static final String selectByItemIdSql = "SELECT * FROM item_photos WHERE item_id=?";
    private static final String insertSql = "INSERT INTO item_photos(path, photo_date, item_id) VALUES (?,?,?)";
    private static final String updateSql = "UPDATE item_photos SET path = ? , "
            + "photo_date = ? "
            + "WHERE id = ?";
    private static final String deleteSql = "DELETE FROM item_photos WHERE item_id = ?";

    public DBItemPhotoDAO(Connection connection) {
        this.connection = connection; // DI
    }



    @Override
    public Optional<ItemPhotoEntity> get(Long id) throws SQLException {
        Optional<ItemPhotoEntity> res = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setLong(1, id);
            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    Path path = Paths.get(rs.getString("path"));
                    LocalDate date = LocalDate.parse(rs.getString("photo_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                    res = Optional.of(new ItemPhotoEntity(id, path, date));
                }
            }
        }
        return res;
    }

    @Override
    public Optional<ItemPhotoEntity> getByItemId(Long itemId) throws SQLException {
        Optional<ItemPhotoEntity> res = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(selectByItemIdSql)) {
            stmt.setLong(1, itemId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
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
                Long id = rs.getLong("id");
                Path path = Paths.get(rs.getString("path"));
                LocalDate date = LocalDate.parse(rs.getString("photo_date"));
                res.add(new ItemPhotoEntity(id, path, date));
            }
        }
        return res;
    }


    @Override
    public ItemPhotoEntity add(ItemPhotoEntity photo, Long itemId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, photo.getPath().toString());
            stmt.setString(2, photo.getDate().toString());
            stmt.setLong(3, itemId);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new ItemPhotoEntity(rs.getLong(1), photo.getPath(), photo.getDate());
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public void deleteByItemId(Long id)throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}