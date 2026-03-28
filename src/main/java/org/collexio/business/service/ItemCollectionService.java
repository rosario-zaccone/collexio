package org.collexio.business.service;

import org.collexio.business.domain.ItemCollection;
import org.collexio.persistence.dao.ItemCollectionDAO;
import org.collexio.persistence.model.ItemCollectionEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ItemCollectionService {
    private final ItemCollectionDAO dao;


    public ItemCollectionService(ItemCollectionDAO dao) {
        this.dao = dao;
    }

    public ItemCollection add(ItemCollection collection) throws SQLException, IOException {
        ItemCollection res;
        ItemCollectionEntity entity = dao.add(collection.toEntity());
        res = ItemCollection.fromEntity(entity);
        return res;
    }


    public ItemCollection get(Long id) throws SQLException {
        Optional<ItemCollectionEntity> res = dao.get(id);
        if (res.isEmpty())
            throw new NoSuchElementException("ItemCollection not found for id: " + id);
        return ItemCollection.fromEntity(res.get());
    }

    public List<ItemCollection> getAll() throws SQLException {
        List<ItemCollection> colls = new ArrayList<>();
        for (ItemCollectionEntity entity : dao.getAll()) {
            colls.add(ItemCollection.fromEntity(entity));
        }
        return colls;
    }

    public void delete(Long id) throws SQLException {
        dao.delete(id);
    }


    public void update(ItemCollection collection) throws SQLException, IOException {
        dao.update(collection.toEntity());
    }

    /* move to another service orch
    public double value(ItemCollection collection) {
        return collection.getData().stream().map(e -> itemService.getSpecService().price(e.getSpec()))
                .reduce(0.0, Double::sum);
    }*/
}
