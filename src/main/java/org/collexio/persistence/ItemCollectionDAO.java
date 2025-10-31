package org.collexio.persistence;

import org.collexio.domain.Item;
import org.collexio.domain.ItemCollection;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemCollectionDAO<T extends Item> {
    void add(ItemCollection<T> collection) throws SQLException;
    Optional<ItemCollection<T>> get(Long id) throws SQLException;
    void update(ItemCollection<T> collection);
    void delete(Long id) throws SQLException;
    List<ItemCollection<T>> getAll() throws SQLException;
}
