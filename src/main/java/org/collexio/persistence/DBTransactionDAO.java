package org.collexio.persistence;

import org.collexio.domain.ItemPhoto;
import org.collexio.domain.Transaction;
import org.collexio.utilities.Utilities;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class DBTransactionDAO implements TransactionDAO {
    private final Connection connection;

    private static final String selectAllSql = "SELECT * FROM item_transactions";
    private static final String selectSql = "SELECT * FROM item_transactions WHERE id=?";
    private static final String insertSql = "INSERT INTO item_transactions(amount, income, transaction_date, item_id) VALUES (?,?,?,?)";
    private static final String updateSql = "UPDATE item_transactions SET amount = ? , "
            + "income = ? ,"
            + "transaction_date = ?"
            + "WHERE id = ?";
    private static final String deleteSql = "DELETE FROM item_transactions WHERE id=?";

    public DBTransactionDAO(Connection connection) {
        this.connection = connection; // DI
    }

    @Override
    public void add(Transaction transaction, int itemId) throws SQLException {
        // add itemId validation TODO
        try (var stmt = connection.prepareStatement(insertSql)) {
            stmt.setDouble(1, transaction.getAmount());
            stmt.setInt(2, transaction.isIncome() ? 1 : 0);
            stmt.setString(3, transaction.getDate().toString());
            stmt.setInt(4, itemId);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Transaction> get(int id) throws SQLException {
        Optional<Transaction> res = Optional.empty();
        if (id <= 0)
            throw new IllegalArgumentException("Invalid id");
        try (var stmt = connection.prepareStatement(selectSql)) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
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
    public void update(Transaction transaction) throws SQLException{
        try (var stmt = connection.prepareStatement(updateSql)) {
            stmt.setDouble(1, transaction.getAmount());
            stmt.setInt(2, transaction.isIncome() ? 1 : 0);
            stmt.setString(3, transaction.getDate().toString());
            stmt.setInt(4, transaction.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException{
        try (var stmt = connection.prepareStatement(deleteSql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Transaction> getAll() throws SQLException{
        List<Transaction> res = new ArrayList<>();
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(selectAllSql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                double amount = rs.getDouble("amount");
                boolean income = rs.getInt("income") == 1;
                LocalDateTime date = LocalDateTime.parse(rs.getString("transaction_date")); //rs.getTimestamp("photo_date").toLocalDateTime(); for postgres
                res.add(new Transaction(id, amount, income, date));
            }
        }
        return res;
    }
}
