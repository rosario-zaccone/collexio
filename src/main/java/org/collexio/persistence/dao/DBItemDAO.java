package org.collexio.persistence.dao;
import org.collexio.persistence.model.*;


import java.sql.*;
import java.util.*;


public class DBItemDAO implements ItemDAO{
    private final Connection connection;

    private final static String selectSql = "SELECT * FROM items WHERE id=?";
    private final static String selectAllSql = "SELECT * FROM items";
    private final static String selectByCollectionId = "SELECT * FROM items WHERE item_collection_id=?";
    private final static String insertSql = "INSERT INTO items(status, item_spec_id, item_collection_id)"
            + "VALUES(?,?,?)";
    private final static String deleteSql = "DELETE FROM items WHERE id=?";
    private static final String updateSql = "UPDATE items SET status = ? , "
            + "item_collection_id = ? "
            + "WHERE id = ?";
    public DBItemDAO(Connection connection) {
        this.connection = connection;
    }


    @Override
    public ItemEntity add(ItemEntity item, Long collectionId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getStatus().getValue());
            stmt.setLong(2, item.getSpec().getId());
            if (collectionId != null) {
                stmt.setLong(3, collectionId);
            } else {
                stmt.setNull(3, java.sql.Types.BIGINT);
            }
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new ItemEntity(rs.getLong(1), item.getStatus(), item.getSpec());
                } else {
                    throw new SQLException("Creating item failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public Optional<ItemEntity> get(Long id) throws SQLException {
        Optional<ItemEntity> res = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ItemStatus status = ItemStatus.fromInt(rs.getInt("status"));
                    ItemEntity item = new ItemEntity(id, status);
                    res = Optional.of(item);
                }
            }
        }
        return res;
    }

    @Override
    public List<ItemEntity> getByCollectionId(Long collectionId) throws SQLException {
        List<ItemEntity> res = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(selectByCollectionId)) {
            stmt.setLong(1, collectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    ItemStatus status = ItemStatus.fromInt(rs.getInt("status"));
                    ItemEntity item = new ItemEntity(id, status);
                    res.add(item);
                }
            }
        }
        return res;
    }

    @Override
    public void update(ItemEntity item, Long collectionId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setInt(1, item.getStatus().getValue());
            if (collectionId != null)
                stmt.setLong(2, collectionId);
            else
                stmt.setNull(2, Types.BIGINT);
            stmt.setLong(3, item.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<ItemEntity> getAll() throws SQLException {
        List<ItemEntity> res = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectAllSql)) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    ItemStatus status = ItemStatus.fromInt(rs.getInt("status"));
                    ItemEntity item = new ItemEntity(id, status);
                    res.add(item);
                }
        }
        return res;
    }

    @Override
    public Optional<Long> getCollectionId(Long id) throws SQLException {
        Optional<Long> res = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long collectionId = rs.getLong("item_collection_id");
                    if (!rs.wasNull())
                        res = Optional.of(collectionId);
                }
            }
        }
        return res;
    }

    public Connection getConnection() {
        return connection;
    }
}