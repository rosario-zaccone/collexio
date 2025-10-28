package org.collexio.persistence;

import org.collexio.domain.Item;
import org.collexio.domain.ItemCollection;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemCollectionDAO<T extends Item> {
    void add(ItemCollection<T> collection);
    Optional<ItemCollection<T>> get(String id);
    void update(ItemCollection<T> collection);
    void delete(String id);
    List<ItemCollection<T>> getAll() throws SQLException;
}
