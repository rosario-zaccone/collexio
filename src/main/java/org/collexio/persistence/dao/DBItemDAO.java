package org.collexio.persistence.dao;
import org.collexio.persistence.model.*;


import java.sql.*;
import java.util.*;


public class DBItemDAO implements ItemDAO{
    private final Connection connection;
    private final ItemSpecDAO itemSpecDAO;
    private final ItemPhotoDAO itemPhotoDAO;
    private final TransactionDAO transactionDAO;

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

    public DBItemDAO(Connection connection, ItemSpecDAO itemSpecDAO, ItemPhotoDAO itemPhotoDAO, TransactionDAO transactionDAO) {
        this.connection = connection;
        this.itemSpecDAO = itemSpecDAO;
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
                stmt.setInt(1, item.getStatus().getValue());
                stmt.setLong(2, item.getDetails().getId());
                stmt.setLong(3, collectionId);
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
                    ItemStatus status = ItemStatus.fromInt(rs.getInt("status"));
                    ItemSpecEntity details = itemSpecDAO.get(rs.getLong("item_spec_id")).get();
                    ItemPhotoEntity photo =  itemPhotoDAO.getByItemId(id).orElseThrow(() -> new NoSuchElementException("No photo for this item"));
                    List<TransactionEntity> transactions = transactionDAO.getByItemId(id);
                    ItemEntity item = new ItemEntity(id, status, photo, details);
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
                    ItemStatus status = ItemStatus.fromInt(rs.getInt("status"));
                    ItemSpecEntity details = itemSpecDAO.get(rs.getLong("item_spec_id")).get();
                    ItemPhotoEntity photo =  itemPhotoDAO.getByItemId(id).orElseThrow(() -> new NoSuchElementException("No photo for this item"));
                    List<TransactionEntity> transactions = transactionDAO.getByItemId(id);
                    ItemEntity item = new ItemEntity(id, status, photo, details);
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
                    long id = rs.getLong("id");
                    ItemStatus status = ItemStatus.fromInt(rs.getInt("status"));
                    ItemSpecEntity details = itemSpecDAO.get(rs.getLong("item_spec_id")).get();
                    ItemPhotoEntity photo =  itemPhotoDAO.getByItemId(id).orElseThrow(() -> new NoSuchElementException("No photo for this item"));
                    List<TransactionEntity> transactions = transactionDAO.getByItemId(id);
                    ItemEntity item = new ItemEntity(id, status, photo, details);
                    transactions.forEach(item::addTransaction);
                    res.add(item);
                }
        }
        return res;
    }
}