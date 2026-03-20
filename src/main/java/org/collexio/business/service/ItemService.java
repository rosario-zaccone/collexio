package org.collexio.business.service;


import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemSpec;
import org.collexio.business.domain.Transaction;
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
    private final ItemPhotoService photoService;
    private final TransactionService transactionService;

    public ItemService(ItemDAO dao, ItemPhotoService photoService, TransactionService transactionService) {
        this.dao = dao;
        this.photoService = photoService;
        this.transactionService = transactionService;
    }

    public void add(Item item, Long collectionId) throws SQLException, IOException {
        // PRE: details and collection prev saved
        Connection conn = null;
        try {
            conn = ((DBItemDAO)dao).getConnection();
            conn.setAutoCommit(false);
            Long id = dao.add(item.toEntity(), collectionId);
            photoService.add(item.getPhoto(), id);
            for (Transaction e : item.getTransactions()) {
                transactionService.addTransaction(e, id);
            }
            conn.commit();
        } catch (SQLException | IOException e) {
            conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }

    public Item get(Long id) throws SQLException {
        Optional<ItemEntity> res = dao.get(id);
        if (res.isEmpty())
            throw new NoSuchElementException("Item not found for id: " + id);
        Item item = Item.fromEntity(res.get());
        item.setPhoto(photoService.getByItemId(id));
        item.setDetails(
                ItemSpec.fromEntity(
                        dao.getItemSpec(id)
                                .orElseThrow(() -> new NoSuchElementException("Item details not found for id: " + id))
                )
        );
        transactionService.getByItemId(id).forEach(item::addTransaction);
        return item;
    }

    public void freeFromCollection(Item item) throws SQLException, IOException {
        dao.update(item.toEntity(), true);
    }

    public List<Item> getByCollectionId(Long collectionId) throws SQLException {
        List<Item> items = new ArrayList<>();
        for (ItemEntity entity: dao.getByCollectionId(collectionId)) {
            items.add(get(entity.getId()));
        }
        return items;
    }

    public void delete(Long id) throws SQLException {
        dao.delete(id);
    }


    public void update(Item item) throws SQLException, IOException {
        //TODO
        //idea: update aggiorna anche le transazioni di item
    }
}

