package org.collexio.business.service;

import org.collexio.business.domain.Transaction;
import org.collexio.persistence.dao.TransactionDAO;
import org.collexio.persistence.model.TransactionEntity;

import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private final TransactionDAO dao;

    public TransactionService(TransactionDAO dao) {
        this.dao = dao;
    }

    public Transaction addTransaction(Transaction transaction, Long itemId) throws SQLException {
        TransactionEntity entity = dao.add(transaction.toEntity(), itemId);
        return Transaction.fromEntity(entity);
    }

    public void updateTransaction(Transaction transaction) throws SQLException {
        dao.update(transaction.toEntity());
    }

    public void deleteTransaction(Transaction transaction) throws SQLException {
        dao.delete(transaction.toEntity().getId());
    }

    public List<Transaction> getByItemId(Long itemId) throws SQLException {
        return dao.getByItemId(itemId).stream().map(Transaction::fromEntity).toList();
    }

}
