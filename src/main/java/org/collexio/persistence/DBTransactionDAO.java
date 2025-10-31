package org.collexio.persistence;

import org.collexio.domain.Item;
import org.collexio.domain.ItemPhoto;
import org.collexio.domain.Transaction;
import org.collexio.utilities.Utilities;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class DBTransactionDAO implements TransactionDAO {
    private final Connection connection;

    private static final String selectAllSql = "SELECT * FROM item_transactions";
    private static final String selectByItemIdSql = "SELECT * FROM item_transactions WHERE item_id = ?";
    private static final String selectSql = "SELECT * FROM item_transactions WHERE id=?";
    private static final String insertSql = "INSERT INTO item_transactions(amount, income, transaction_date, item_id) VALUES (?,?,?,?)";
    private static final String updateSqlNoItem = "UPDATE item_transactions SET amount = ? , "
            + "income = ? ,"
            + "transaction_date = ?,"
            + "item_id = ? "
            + "WHERE id = ?";
    private static final String updateSql = "UPDATE item_transactions SET amount = ? , "
            + "income = ? ,"
            + "transaction_date = ?"
            + "WHERE id = ?";
    private static final String deleteSql = "DELETE FROM item_transactions WHERE id=?";

    public DBTransactionDAO(Connection connection) {
        this.connection = connection; // DI
    }

    @Override
    public void add(Transaction transaction, Long itemId) throws SQLException {
        if (itemId <= 0)
            throw new IllegalArgumentException("Invalid id");
        try (var stmt = connection.prepareStatement(insertSql)) {
            stmt.setDouble(1, transaction.getAmount());
            stmt.setInt(2, transaction.isIncome() ? 1 : 0);
            stmt.setString(3, transaction.getDate().toString());
            stmt.setLong(4, itemId);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Transaction> get(Long id) throws SQLException {
        Optional<Transaction> res = Optional.empty();
        if (id <= 0)
            throw new IllegalArgumentException("Invalid id");
        try (var stmt = connection.prepareStatement(selectSql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                double amount = rs.getDouble("amount");
                boolean income = rs.getInt("income") == 1;
                LocalDateTime date = LocalDateTime.parse(rs.getString("transaction_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                res = Optional.of(new Transaction(id, amount, income, date));
            }
            rs.close();
        }
        return res;
    }

    @Override
    public void update(Transaction transaction, boolean noItem) throws SQLException{
        String prompt = updateSql;
        if (noItem)
            prompt = updateSqlNoItem;
        try (var stmt = connection.prepareStatement(prompt)) {
            stmt.setDouble(1, transaction.getAmount());
            stmt.setInt(2, transaction.isIncome() ? 1 : 0);
            stmt.setString(3, transaction.getDate().toString());
            if (noItem) {
                stmt.setNull(4, Types.INTEGER);
                stmt.setLong(5, transaction.getId());
            } else
                stmt.setLong(4, transaction.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException{
        if (id <= 0)
            throw new IllegalArgumentException("Invalid id");
        try (var stmt = connection.prepareStatement(deleteSql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Transaction> getAll() throws SQLException{
        List<Transaction> res = new ArrayList<>();
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(selectAllSql)) {
            while (rs.next()) {
                long id = rs.getLong("id");
                double amount = rs.getDouble("amount");
                boolean income = rs.getInt("income") == 1;
                LocalDateTime date = LocalDateTime.parse(rs.getString("transaction_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                res.add(new Transaction(id, amount, income, date));
            }
        }
        return res;
    }

    @Override
    public List<Transaction> getByItemId(Long itemId) throws SQLException {
        List<Transaction> res = new ArrayList<>();
        try (var stmt = connection.prepareStatement(selectByItemIdSql)) {
            stmt.setLong(1, itemId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    double amount = rs.getDouble("amount");
                    boolean income = rs.getInt("income") == 1;
                    LocalDateTime date = LocalDateTime.parse(rs.getString("transaction_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                    res.add(new Transaction(id, amount, income, date));
                }
            }
        }
        return res;
    }
}
