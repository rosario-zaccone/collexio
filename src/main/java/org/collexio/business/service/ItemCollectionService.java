package org.collexio.business.service;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemCollection;
import org.collexio.persistence.dao.DBItemCollectionDAO;
import org.collexio.persistence.dao.ItemCollectionDAO;
import org.collexio.persistence.model.ItemCollectionEntity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ItemCollectionService {
    private final ItemCollectionDAO dao;
    private final ItemService itemService;


    public ItemCollectionService(ItemCollectionDAO dao, ItemService itemService) {
        this.dao = dao;
        this.itemService = itemService;
    }

    // add get getAll delete update
    public ItemCollection add(ItemCollection collection) throws SQLException, IOException {
        Connection connection = ((DBItemCollectionDAO) dao).getConnection();
        ItemCollection res;
        try {
            connection.setAutoCommit(false);
            ItemCollectionEntity entity = dao.add(collection.toEntity());
            res = ItemCollection.fromEntity(entity);
            for (Item e : collection.getData()) {
                res.addItem(itemService.add(e, res.getId()));
            }
            connection.commit();
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            connection.rollback();
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
        return res;
    }


    public ItemCollection get(Long id) throws SQLException {
        Optional<ItemCollectionEntity> res = dao.get(id);
        if (res.isEmpty())
            throw new NoSuchElementException("ItemCollection not found for id: " + id);
        ItemCollection collection = ItemCollection.fromEntity(res.get());
        List<Item> items = itemService.getByCollectionId(id);
        items.forEach(collection::addItem);
        return collection;
    }

    public List<ItemCollection> getAll() throws SQLException {
        List<ItemCollection> colls = new ArrayList<>();
        for (ItemCollectionEntity entity : dao.getAll()) {
            colls.add(get(entity.getId()));
        }
        return colls;
    }

    public void delete(Long id) throws SQLException {
        dao.delete(id);
    }


    public void update(ItemCollection collection) throws SQLException, IOException {
        //TODO
        //idea: update aggiorna anche gli item usando itemservice
    }

    public double value(ItemCollection collection) {
        return collection.getData().stream().map(e -> itemService.getSpecService().price(e.getSpec()))
                .reduce(0.0, Double::sum);
    }
}
