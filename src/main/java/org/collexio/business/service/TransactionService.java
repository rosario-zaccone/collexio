package org.collexio.business.service;

import org.collexio.business.domain.Transaction;
import org.collexio.persistence.dao.TransactionDAO;
import org.collexio.persistence.entity.TransactionEntity;

import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private final TransactionDAO dao;

    public TransactionService(TransactionDAO dao) {
        this.dao = dao;
    }

    public Transaction add(Transaction transaction, Long itemId) throws SQLException {
        // item can be bought, sold and rebought from the buyer
        // can enter income only if the last transaction is an expense
        // can enter expense only if there are no transactions or the last transaction is an income
        List<Transaction> transactions = getByItemId(itemId);
        if (transaction.isIncome()) {
            if (transactions.isEmpty() || transactions.get(transactions.size() - 1).isIncome())
                throw new IllegalArgumentException("The last transaction is an income, you can't enter another income transaction");
        } else {
            if (!transactions.isEmpty() && !transactions.get(transactions.size() - 1).isIncome())
                throw new IllegalArgumentException("The last transaction is an expense, you can't enter another expense transaction");
        }
        TransactionEntity entity = dao.add(transaction.toEntity(), itemId);
        return Transaction.fromEntity(entity);
    }

    public void update(Transaction transaction) throws SQLException {
        dao.update(transaction.toEntity());
    }

    public void delete(Long id) throws SQLException {
        dao.delete(id);
    }

    public List<Transaction> getByItemId(Long itemId) throws SQLException {
        return dao.getByItemId(itemId).stream().map(Transaction::fromEntity).toList();
    }

    public List<Transaction> getAll() throws SQLException {
        return dao.getAll().stream().map(Transaction::fromEntity).toList();
    }

    public Long getItemId(Long id) throws SQLException {
        return dao.getItemId(id);
    }

}
