package org.collexio.persistence.dao;

import org.collexio.persistence.entity.TransactionEntity;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;


public class DBTransactionDAO implements TransactionDAO {
    private final Connection connection;

    private static final String selectAllSql = "SELECT * FROM item_transactions ORDER BY transaction_date";
    private static final String selectByItemIdSql = "SELECT * FROM item_transactions WHERE item_id = ? ORDER BY transaction_date";
    private static final String selectSql = "SELECT * FROM item_transactions WHERE id=?";
    private static final String insertSql = "INSERT INTO item_transactions(amount, income, transaction_date, item_id) VALUES (?,?,?,?)";
    private static final String updateSql = "UPDATE item_transactions SET amount = ? , "
            + "income = ? ,"
            + "transaction_date = ? "
            + "WHERE id = ?";
    private static final String deleteSql = "DELETE FROM item_transactions WHERE id=?";

    public DBTransactionDAO(Connection connection) {
        this.connection = connection; // DI
    }

    @Override
    public TransactionEntity add(TransactionEntity transaction, Long itemId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setDouble(1, transaction.getAmount());
            stmt.setInt(2, transaction.isIncome() ? 1 : 0);
            stmt.setString(3, transaction.getDate().toString());
            stmt.setLong(4, itemId);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new TransactionEntity(rs.getLong(1), transaction.getAmount(), transaction.isIncome(), transaction.getDate());
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
        }

    }

    @Override
    public Optional<TransactionEntity> get(Long id) throws SQLException {
        Optional<TransactionEntity> res = Optional.empty();
        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double amount = rs.getDouble("amount");
                    boolean income = rs.getInt("income") == 1;
                    LocalDate date = LocalDate.parse(rs.getString("transaction_date"));
                    res = Optional.of(new TransactionEntity(id, amount, income, date));
                }
            }
        }
        return res;
    }

    @Override
    public void update(TransactionEntity transaction) throws SQLException{
        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setDouble(1, transaction.getAmount());
            stmt.setInt(2, transaction.isIncome() ? 1 : 0);
            stmt.setString(3, transaction.getDate().toString());
            stmt.setLong(4, transaction.getId());
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
    public List<TransactionEntity> getAll() throws SQLException{
        List<TransactionEntity> res = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllSql)) {
            while (rs.next()) {
                long id = rs.getLong("id");
                double amount = rs.getDouble("amount");
                boolean income = rs.getInt("income") == 1;
                LocalDate date = LocalDate.parse(rs.getString("transaction_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                res.add(new TransactionEntity(id, amount, income, date));
            }
        }
        return res;
    }

    @Override
    public List<TransactionEntity> getByItemId(Long itemId) throws SQLException {
        List<TransactionEntity> res = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(selectByItemIdSql)) {
            stmt.setLong(1, itemId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    double amount = rs.getDouble("amount");
                    boolean income = rs.getInt("income") == 1;
                    LocalDate date = LocalDate.parse(rs.getString("transaction_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                    res.add(new TransactionEntity(id, amount, income, date));
                }
            }
        }
        return res;
    }

    @Override
    public Long getItemId(Long id) throws SQLException {
        Long itemId = null;
        try (PreparedStatement stmt = connection.prepareStatement(selectSql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    itemId = rs.getLong("item_id");
                }
            }
        }
        return itemId;
    }
}
