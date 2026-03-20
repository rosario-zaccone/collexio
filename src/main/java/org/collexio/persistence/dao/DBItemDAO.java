package org.collexio.persistence.dao;
import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemSpec;
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
    private static final String updateSqlNoCollection = "UPDATE items SET name = ? , "
            + "status = ? ,"
            + "item_collection_id = ? "
            + "WHERE id = ?";
    private static final String updateSql = "UPDATE items SET status = ? "
            + "WHERE id = ?";
    private static final String selectSpecSql =
            "SELECT item_specs.id AS spec_id, item_specs.type, item_specs.name, item_specs.description " +
                    "FROM items INNER JOIN item_specs ON items.item_spec_id = item_specs.id " +
                    "WHERE items.id = ?";

    public DBItemDAO(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Long add(ItemEntity item, Long collectionId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getStatus().getValue());
            stmt.setLong(2, item.getSpec().getId());
            stmt.setLong(3, collectionId);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
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
    public void update(ItemEntity item, boolean noCollection) throws SQLException {
        String prompt = updateSql;
        if (noCollection)
            prompt = updateSqlNoCollection;
        try (PreparedStatement stmt = connection.prepareStatement(prompt)) {
            stmt.setInt(1, item.getStatus().getValue());
            if (noCollection) {
                stmt.setNull(2, Types.BIGINT);
                stmt.setLong(3, item.getId());
            } else
                stmt.setLong(2, item.getId());
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
    public Optional<ItemSpecEntity> getItemSpec(Long id) throws SQLException {
        Optional<ItemSpecEntity> res = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(selectSpecSql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long specId = rs.getLong("spec_id");
                    ItemType type = ItemType.fromInt(rs.getInt("type"));
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    res = Optional.of(new ItemSpecEntity(specId, type, name, description));
                }
            }
        }
        return res;
    }

    public Connection getConnection() {
        return connection;
    }
}