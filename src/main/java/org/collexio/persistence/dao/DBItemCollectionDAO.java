package org.collexio.persistence.dao;

import org.collexio.persistence.model.ItemCollectionEntity;
import org.collexio.persistence.model.ItemEntity;
import org.collexio.persistence.model.TransactionEntity;

import java.sql.*;
import java.util.*;

public class DBItemCollectionDAO implements ItemCollectionDAO {
    private final Connection connection;

    private final static String selectSql = "SELECT * FROM item_collections WHERE id=?";
    private final static String selectAllSql = "SELECT * FROM item_collections";
    private final static String insertSql = "INSERT INTO item_collections(name) "
            + "VALUES(?)";
    private final static String deleteSql = "DELETE FROM item_collections WHERE id=?";
    private static final String updateSql = "UPDATE item_collections SET name = ? "
            + "WHERE id = ?";

    public DBItemCollectionDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ItemCollectionEntity add(ItemCollectionEntity collection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, collection.getName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new ItemCollectionEntity(rs.getLong(1), collection.getName());
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
        }

    }

    @Override
    public Optional<ItemCollectionEntity> get(Long id) throws SQLException {
        Optional<ItemCollectionEntity> res = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    ItemCollectionEntity collection = new ItemCollectionEntity(id, name);
                    res = Optional.of(collection);
                }
            }
        }
        return res;
    }

    @Override
    public void update(ItemCollectionEntity collection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setString(1, collection.getName());
            stmt.setLong(2, collection.getId());
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
    public List<ItemCollectionEntity> getAll() throws SQLException {
        List<ItemCollectionEntity> res = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllSql)) {
            if (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                ItemCollectionEntity collection = new ItemCollectionEntity(id, name);
                res.add(collection);
            }
        }
        return res;
    }
}