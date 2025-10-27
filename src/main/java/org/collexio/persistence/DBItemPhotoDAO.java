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
import java.util.Optional;

public class DBItemPhotoDAO implements ItemPhotoDAO {
    private Connection connection;

    private static final String selectAllSql = "SELECT * FROM item_photos";
    private static final String selectSql = "SELECT * FROM item_photos WHERE id=?";

    public DBItemPhotoDAO(Connection connection) throws SQLException, IOException {
        this.connection = connection; // DI
    }

    @Override
    public Optional<ItemPhoto> get(String id) throws SQLException {
        Optional<ItemPhoto> res = Optional.empty();
        if (!Utilities.validateId("P-", id))
            throw new IllegalArgumentException("Invalid id");
        try (var stmt = connection.prepareStatement(selectSql)) {
            stmt.setInt(1, Integer.parseInt(id.substring(2)));
            var rs = stmt.executeQuery();
            while (rs.next()) {
                int rawId = rs.getInt("id");
                Path path = Paths.get(rs.getString("path"));
                LocalDateTime date = LocalDateTime.parse(rs.getString("photo_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                res = Optional.of(new ItemPhoto("P-" + rawId, path, date));
            }
            rs.close();
        }
        return res;
    }

    @Override
    public void update(ItemPhoto photo) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<ItemPhoto> getAll() throws SQLException {
        List<ItemPhoto> res = new ArrayList<>();
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(selectAllSql)) {
            while (rs.next()) {
                int rawId = rs.getInt("id");
                Path path = Paths.get(rs.getString("path"));
                LocalDateTime date = LocalDateTime.parse(rs.getString("photo_date"));
                res.add(new ItemPhoto("P-" + rawId, path, date));
            }
        }
        return res;
    }

    @Override
    public void add(ItemPhoto photo) {

    }
}
