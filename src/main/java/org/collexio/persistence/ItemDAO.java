package org.collexio.persistence;

import org.collexio.domain.Item;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemDAO {
    void add(Item item, int collectionId) throws SQLException;
    Optional<Item> get(int id);
    void update(Item item);
    void delete(int id);
    List<Item> getAll() throws SQLException;
}
