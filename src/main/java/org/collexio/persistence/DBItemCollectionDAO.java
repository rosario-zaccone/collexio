package org.collexio.persistence;

import org.collexio.domain.Item;
import org.collexio.domain.ItemCollection;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DBItemCollectionDAO<T extends Item> implements ItemCollectionDAO<T> {
    @Override
    public void add(ItemCollection<T> collection) {

    }

    @Override
    public Optional<ItemCollection<T>> get(String id) {
        return Optional.empty();
    }

    @Override
    public void update(ItemCollection<T> collection) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<ItemCollection<T>> getAll() throws SQLException {
        return List.of();
    }
}
