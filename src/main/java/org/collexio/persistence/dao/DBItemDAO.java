package org.collexio.persistence.dao;

import org.collexio.persistence.model.ItemEntity;
import org.collexio.persistence.model.ItemPhotoEntity;
import org.collexio.persistence.model.ItemType;
import org.collexio.persistence.model.TransactionEntity;


import java.sql.*;
import java.util.*;


public class DBItemDAO implements ItemDAO{
    private final Connection connection;
    private final ItemPhotoDAO itemPhotoDAO;
    private final TransactionDAO transactionDAO;

    private final static String selectSql = "SELECT * FROM items WHERE id=?";
    private final static String selectAllSql = "SELECT * FROM items";
    private final static String selectByCollectionId = "SELECT * FROM items WHERE item_collection_id=?";
    private final static String insertSql = "INSERT INTO items(name, quantity, description, type, item_collection_id)"
            + "VALUES(?,?,?,?,?)";
    private final static String deleteSql = "DELETE FROM items WHERE id=?";
    private static final String updateSqlNoCollection = "UPDATE items SET name = ? , "
            + "quantity = ? ,"
            + "description = ? ,"
            + "item_collection_id = ? "
            + "WHERE id = ?";
    private static final String updateSql = "UPDATE items SET name = ? , "
            + "quantity = ? ,"
            + "description = ? "
            + "WHERE id = ?";

    public DBItemDAO(Connection connection, ItemPhotoDAO itemPhotoDAO, TransactionDAO transactionDAO) {
        this.connection = connection;
        this.itemPhotoDAO = itemPhotoDAO;
        this.transactionDAO = transactionDAO;
    }


    @Override
    public void add(ItemEntity item, Long collectionId) throws SQLException {
        boolean transactionOwner = false;
        if (connection.getAutoCommit()) {
            connection.setAutoCommit(false);
            transactionOwner = true;
        }
        try {
            try (PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, item.getName());
                stmt.setInt(2, item.getQuantity());
                stmt.setString(3, item.getDescription());
                stmt.setInt(4, item.getType().ordinal());
                stmt.setLong(5, collectionId);
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        long itemId = Math.toIntExact(keys.getLong(1));
                        itemPhotoDAO.add(item.getPhoto(), itemId);
                        for (TransactionEntity e : item.getTransactions()) {
                            transactionDAO.add(e, itemId);
                        }
                    }
                }
            }
            if (transactionOwner) {
                connection.commit();
            }
        } catch (SQLException ex) {
            if (transactionOwner) {
                connection.rollback();
            }
            throw ex;
        } finally {
            if (transactionOwner) {
                connection.setAutoCommit(true);
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
                    int type = rs.getInt("type");
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    String description = rs.getString("description");
                    ItemPhotoEntity photo =  itemPhotoDAO.getByItemId(id).orElseThrow(() -> new NoSuchElementException("No photo for this item"));
                    List<TransactionEntity> transactions = transactionDAO.getByItemId(id);
                    ItemEntity item = new ItemEntity(id, ItemType.fromInt(type), name, quantity, photo, description);
                    transactions.forEach(item::addTransaction);
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
                    long id = rs.getLong("id");
                    int type = rs.getInt("type");
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    String description = rs.getString("description");
                    ItemPhotoEntity photo = itemPhotoDAO.getByItemId(id).orElseThrow(() -> new NoSuchElementException("No photo for this item"));
                    List<TransactionEntity> transactions = transactionDAO.getByItemId(id);
                    ItemEntity item = new ItemEntity(id, ItemType.fromInt(type), name, quantity, photo, description);
                    transactions.forEach(item::addTransaction);
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
            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getQuantity());
            stmt.setString(3, item.getDescription());
            if (noCollection) {
                stmt.setNull(4, Types.BIGINT);
                stmt.setLong(5, item.getId());
            } else
                stmt.setLong(4, item.getId());
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
                    long id = rs.getLong("id");
                    int type = rs.getInt("type");
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    String description = rs.getString("description");
                    ItemPhotoEntity photo = itemPhotoDAO.getByItemId(id).orElseThrow(() -> new NoSuchElementException("No photo for this item"));
                    List<TransactionEntity> transactions = transactionDAO.getByItemId(id);
                    ItemEntity item = new ItemEntity(id, ItemType.fromInt(type), name, quantity, photo, description);
                    transactions.forEach(item::addTransaction);
                    res.add(item);
                }
        }
        return res;
    }
}
