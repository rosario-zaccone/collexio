package org.collexio.business.service;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemCollection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemCollectionOrchestrator {
    // get collection with items (for ui reasons and for business methods)
    private final ItemService itemService;
    private final ItemCollectionService collectionService;

    public ItemCollectionOrchestrator(ItemService itemService, ItemCollectionService collectionService) {
        this.itemService = itemService;
        this.collectionService = collectionService;
    }

    public ItemCollection getFullCollection(Long id) throws SQLException {
        ItemCollection collection = collectionService.get(id);
        List<Item> items = itemService.getByCollectionId(id);
        items.forEach(collection::addItem);
        return collection;
    }

    public List<ItemCollection> getAllFullCollections() throws SQLException {
        var collectins = collectionService.getAll();
        List<ItemCollection> res = new ArrayList<>();
        for (ItemCollection collection: collectins)
            res.add(getFullCollection(collection.getId()));
        return res;
    }
}
