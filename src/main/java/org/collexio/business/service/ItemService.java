package org.collexio.business.service;


import org.collexio.business.domain.Item;
import org.collexio.persistence.dao.DBItemDAO;
import org.collexio.persistence.dao.ItemDAO;
import org.collexio.persistence.model.ItemEntity;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


public class ItemService {
    private final ItemDAO dao;

    public ItemService(ItemDAO dao) {
        this.dao = dao;
    }

    public Item add(Item item, Long collectionId) throws SQLException, IOException {
        Item res;
        ItemEntity entity = dao.add(item.toEntity(), collectionId);
        res = Item.fromEntity(entity);
        return res;
    }


    public Item get(Long id) throws SQLException {
        Optional<ItemEntity> res = dao.get(id);
        if (res.isEmpty())
            throw new NoSuchElementException("Item not found for id: " + id);
        return Item.fromEntity(res.get());
    }

    public void freeFromCollection(Item item) throws SQLException {
        dao.update(item.toEntity(), null);
    }

    public List<Item> getByCollectionId(Long collectionId) throws SQLException {
        List<Item> items = new ArrayList<>();
        for (ItemEntity entity : dao.getByCollectionId(collectionId)) {
            items.add(Item.fromEntity(entity));
        }
        return items;
    }

    public List<Item> getAll() throws SQLException {
        List<Item> items = new ArrayList<>();
        for (ItemEntity entity : dao.getAll()) {
            items.add(Item.fromEntity(entity));
        }
        return items;
    }

    public void delete(Long id) throws SQLException {
        dao.delete(id);
    }


    public void update(Item item, Long collectionId) throws SQLException, IOException {
        dao.update(item.toEntity(), collectionId);
    }

    public Optional<Long> getCollectionId(Long id) throws SQLException {
        return dao.getCollectionId(id);
    }

    public Connection getConnection() {
        return ((DBItemDAO)dao).getConnection();
    }
}

/*
public Item add(Item item, Long collectionId) throws SQLException, IOException {
        // PRE: details and collection prev saved
        Connection connection = ((DBItemDAO) dao).getConnection();
        boolean transactionOwner = false;
        if (connection.getAutoCommit()) {
            connection.setAutoCommit(false);
            transactionOwner = true;
        }
        Item res;
        try {
            connection.setAutoCommit(false);
            ItemEntity entity = dao.add(item.toEntity(), collectionId);
            res = Item.fromEntity(entity);
            res.setPhoto(photoService.add(item.getPhoto(), entity.getId()));
            for (Transaction e : item.getTransactions()) {
                res.addTransaction(transactionService.addTransaction(e, entity.getId()));
            }
            if (transactionOwner)
                connection.commit();
        } catch (SQLException | IOException e) {
            if (transactionOwner)
                connection.rollback();
            throw e;
        } finally {
            if (transactionOwner) {
                connection.setAutoCommit(true);
            }
        }
        return res;
    }
 */
