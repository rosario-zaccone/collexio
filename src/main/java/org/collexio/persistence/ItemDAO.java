package org.collexio.persistence;

import org.collexio.domain.Item;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemDAO {
    void add(Item item, int collectionId) throws SQLException;
    Optional<Item> get(int id) throws SQLException;
    void update(Item item, boolean noCollection) throws SQLException; // noCollection = true -> remove the item from the collection
    void delete(int id) throws SQLException;
    List<Item> getAll() throws SQLException;
}
