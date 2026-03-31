package org.collexio.business.service;

import org.checkerframework.checker.units.qual.A;
import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemPhoto;
import org.collexio.business.domain.Transaction;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemOrchestrator {
    private final ItemService itemService;
    private final ItemPhotoService photoService;
    private final TransactionService transactionService;
    private final ItemSpecService specService;

    public ItemOrchestrator(ItemService itemService, ItemPhotoService photoService, TransactionService transactionService, ItemSpecService specService) {
        this.itemService = itemService;
        this.photoService = photoService;
        this.transactionService = transactionService;
        this.specService = specService;
    }

    public Item addWithPhoto(Item item, Long collectionId, ItemPhoto photo) throws SQLException, IOException {
        Item nItem;
        Connection connection = itemService.getConnection();
        try {
            connection.setAutoCommit(false);
            nItem = itemService.add(item, collectionId);
            nItem.setPhoto(photoService.add(photo, item.getId()));
        } catch (SQLException | IOException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
        return nItem;
    }

    public Item updateWithPhoto(Item item, Long collectionId, ItemPhoto photo) throws SQLException, IOException {
        Connection connection = itemService.getConnection();
        Item nItem = new Item(item);
        try {
            connection.setAutoCommit(false);
            itemService.update(item, collectionId);
            nItem.setPhoto(photoService.update(photo, item.getId()));
        } catch (SQLException | IOException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
        return nItem;
    }

    public Item getFullItem(Long id) throws SQLException {
        Item item = itemService.get(id);
        item.setPhoto(photoService.getByItemId(id));
        item.setSpec(specService.getByItemId(id));
        List<Transaction> transactions = transactionService.getByItemId(id);
        transactions.forEach(item::addTransaction);
        return item;
    }

    public List<Item> getAllFullItems() throws SQLException {
        var items = itemService.getAll();
        List<Item> res = new ArrayList<>();
        for (Item item: items)
            res.add(getFullItem(item.getId()));
        return res;
    }

}
