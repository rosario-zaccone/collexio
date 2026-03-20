package org.collexio.persistence.dao;

import org.collexio.persistence.model.ItemSpecEntity;
import org.collexio.persistence.model.ItemType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBItemSpecDAO implements ItemSpecDAO {
    private final Connection connection;

    private static final String selectAllSql = "SELECT * FROM item_specs";
    private static final String selectSql = "SELECT * FROM item_specs WHERE id=?";
    private static final String insertSql = "INSERT INTO item_specs(type, name, description) VALUES (?,?,?)";
    private static final String updateSql = "UPDATE item_specs SET type = ? , "
            + "name = ? ,"
            + "description = ? "
            + "WHERE id = ?";
    private static final String deleteSql = "DELETE FROM item_specs WHERE id=?";

    public DBItemSpecDAO(Connection connection) {
        this.connection = connection; // DI
    }

    @Override
    public void add(ItemSpecEntity spec) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
            stmt.setInt(1, spec.getType().getValue());
            stmt.setString(2, spec.getName());
            stmt.setString(3, spec.getDescription());
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<ItemSpecEntity> get(Long id) throws SQLException {
        Optional<ItemSpecEntity> res = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ItemType type = ItemType.fromInt(rs.getInt("type"));
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    res = Optional.of(new ItemSpecEntity(id, type, name, description));
                }
            }
        }
        return res;
    }

    @Override
    public void update(ItemSpecEntity spec) throws SQLException{
        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setInt(1, spec.getType().getValue());
            stmt.setString(2, spec.getName());
            stmt.setString(3, spec.getDescription());
            stmt.setLong(4, spec.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException{
        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<ItemSpecEntity> getAll() throws SQLException{
        List<ItemSpecEntity> res = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllSql)) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                ItemType type = ItemType.fromInt(rs.getInt("type"));
                String name = rs.getString("name");
                String description = rs.getString("description");
                res.add(new ItemSpecEntity(id, type, name, description));
            }
        }
        return res;
    }
}
