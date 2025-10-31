package org.collexio.persistence;

import org.collexio.domain.*;
import org.collexio.utilities.ItemFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


public class DBItemDAO implements ItemDAO{
    private final Connection connection;

    private final static String selectSql = "SELECT * FROM items WHERE id=?";
    private final static String selectAllSql = "SELECT * FROM items";
    private final static String selectByCollectionId = "SELECT * FROM items WHERE item_collection_id=?";
    private final static String insertSql = "INSERT INTO items(name, quantity, description, type, second_name, item_collection_id)"
            + "VALUES(?,?,?,?,?,?)";
    private final static String deleteSql = "DELETE FROM items WHERE id=?";
    private static final String updateSqlNoCollection = "UPDATE items SET name = ? , "
            + "quantity = ? ,"
            + "description = ? ,"
            + "second_name=?,"
            + "item_collection_id = ?"
            + "WHERE id = ?";
    private static final String updateSql = "UPDATE items SET name = ? , "
            + "quantity = ? ,"
            + "description = ? ,"
            + "second_name=?"
            + "WHERE id = ?";

    public DBItemDAO(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void add(Item item, Long collectionId) throws SQLException {
        boolean transactionOwner = false;
        int type = 0;
        if (item.getClass() == TechItem.class)
            type = 1;
        else if (item.getClass() == Book.class)
            type = 2;
        if (connection.getAutoCommit()) {
            connection.setAutoCommit(false);
            transactionOwner = true;
        }
        try {
            try (PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, item.getName());
                stmt.setInt(2, item.getQuantity());
                stmt.setString(3, item.getDescription());
                stmt.setInt(4, type);
                if (type == 0)
                    stmt.setString(5, ((Plant)(item)).getScientificName());
                else
                    stmt.setNull(5, Types.VARCHAR);
                stmt.setLong(6, collectionId);
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        long itemId = Math.toIntExact(keys.getLong(1));
                        DBItemPhotoDAO itemPhotoDAO = new DBItemPhotoDAO(connection);
                        itemPhotoDAO.add(item.getPhoto(), itemId);
                        DBTransactionDAO transactionDAO = new DBTransactionDAO(connection);
                        for (Transaction e : item.getTransactions()) {
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
    public Optional<Item> get(Long id) throws SQLException {
        if (id <= 0)
            throw new IllegalArgumentException("Invalid id");
        Optional<Item> res = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int type = rs.getInt("type");
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    String description = rs.getString("description");
                    String second_name = rs.getString("second_name");
                    ItemPhoto photo = (new DBItemPhotoDAO(connection)).getByItemId(id).orElseThrow(() -> new NoSuchElementException("No photo for this item"));
                    List<Transaction> transactions = (new DBTransactionDAO(connection)).getByItemId(id);
                    Item item = ItemFactory.getItem(type, id, name, quantity, photo, second_name);
                    item.setDescription(description);
                    transactions.forEach(item::addTransaction);
                    res = Optional.of(item);
                }
            }
        }
        return res;
    }

    @Override
    public List<Item> getByCollectionId(Long collectionId) throws SQLException {
        List<Item> res = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(selectByCollectionId)) {
            stmt.setLong(1, collectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    int type = rs.getInt("type");
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    String description = rs.getString("description");
                    String second_name = rs.getString("second_name");
                    ItemPhoto photo = (new DBItemPhotoDAO(connection)).getByItemId(id).orElseThrow(() -> new NoSuchElementException("No photo for this item"));
                    List<Transaction> transactions = (new DBTransactionDAO(connection)).getByItemId(id);
                    Item item = ItemFactory.getItem(type, id, name, quantity, photo, second_name);
                    item.setDescription(description);
                    transactions.forEach(item::addTransaction);
                    res.add(item);
                }
            }
        }
        return res;
    }

    @Override
    public void update(Item item, boolean noCollection) throws SQLException {
        String prompt = updateSql;
        if (noCollection)
            prompt = updateSqlNoCollection;
        try (var stmt = connection.prepareStatement(prompt)) {
            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getQuantity());
            stmt.setString(3, item.getDescription());
            if (item.getClass() == Plant.class)
                stmt.setString(4, ((Plant) item).getScientificName());
            else
                stmt.setNull(4, Types.VARCHAR);
            if (noCollection) {
                stmt.setNull(5, Types.INTEGER);
                stmt.setLong(6, item.getId());
            } else
                stmt.setLong(5, item.getId());
            stmt.executeUpdate();
        }
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
    public List<Item> getAll() throws SQLException {
        List<Item> res = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectAllSql)) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    int type = rs.getInt("type");
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    String description = rs.getString("description");
                    String second_name = rs.getString("second_name");
                    ItemPhoto photo = (new DBItemPhotoDAO(connection)).getByItemId(id).orElseThrow(() -> new NoSuchElementException("No photo for this item"));
                    List<Transaction> transactions = (new DBTransactionDAO(connection)).getByItemId(id);
                    Item item = ItemFactory.getItem(type, id, name, quantity, photo, second_name);
                    item.setDescription(description);
                    transactions.forEach(item::addTransaction);
                    res.add(item);
                }
        }
        return res;
    }
}
