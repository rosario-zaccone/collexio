package org.collexio.persistence;

import org.collexio.domain.*;
import org.collexio.utilities.ItemFactory;

import java.sql.*;
import java.util.*;

public class DBItemCollectionDAO<T extends Item> implements ItemCollectionDAO<T> {
    private final Connection connection;

    private final static String selectSql = "SELECT * FROM item_collections WHERE id=?";
    private final static String selectAllSql = "SELECT * FROM item_collections";
    private final static String insertSql = "INSERT INTO item_collections(name)"
            + "VALUES(?)";
    private final static String deleteSql = "DELETE FROM item_collections WHERE id=?";
    private static final String updateSql = "UPDATE item_collections SET name = ?"
            + "WHERE id = ?";

    public DBItemCollectionDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(ItemCollection<T> collection) throws SQLException {
        connection.setAutoCommit(false);
        try {
            try (PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, collection.getName());
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        long collectionId = Math.toIntExact(keys.getLong(1));
                        DBItemDAO itemDAO = new DBItemDAO(connection);
                        for (Item item: collection.getData()) {
                            itemDAO.add(item, collectionId);
                        }
                    }
                }
            }
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public Optional<ItemCollection<T>> get(Long id) throws SQLException {
        if (id <= 0)
            throw new IllegalArgumentException("Invalid id");
        Optional<ItemCollection<T>> res = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    List<Item> items = (new DBItemDAO(connection)).getByCollectionId(id);
                    ItemCollection<T> collection = new ItemCollection<>(id, name);
                    for (Item item: items) {
                        collection.addItem((T) item);
                    }
                    res = Optional.of(collection);
                }
            }
        }
        return res;
    }

    @Override
    public void update(ItemCollection<T> collection) {

    }

    @Override
    public void delete(Long id) throws SQLException {
        if (id <= 0)
            throw new IllegalArgumentException("Invalid id");
        try (var stmt = connection.prepareStatement(deleteSql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<ItemCollection<T>> getAll() throws SQLException {
        List<ItemCollection<T>> res = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllSql)) {
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                List<Item> items = (new DBItemDAO(connection)).getByCollectionId(id);
                ItemCollection<T> collection = new ItemCollection<>(name);
                for (Item item: items) {
                    collection.addItem((T) item);
                }
                res.add(collection);
            }
        }
        return res;
    }
}
