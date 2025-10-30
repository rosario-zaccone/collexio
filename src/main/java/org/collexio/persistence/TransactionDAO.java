package org.collexio.persistence;

import org.collexio.domain.Item;
import org.collexio.domain.Transaction;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TransactionDAO {
    void add(Transaction transaction, int itemId) throws SQLException;
    Optional<Transaction> get(int id) throws SQLException;
    void update(Transaction transaction, boolean noItem) throws SQLException;
    void delete(int id) throws SQLException;
    List<Transaction> getAll() throws SQLException;
    List<Transaction> getByItemId(int itemId) throws SQLException;
}
