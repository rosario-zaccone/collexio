package org.collexio.persistence;

import org.collexio.business.domain.ItemCollection;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemCollectionDAO {
    void add(ItemCollection collection) throws SQLException;
    Optional<ItemCollection> get(Long id) throws SQLException;
    void update(ItemCollection collection);
    void delete(Long id) throws SQLException;
    List<ItemCollection> getAll() throws SQLException;
}
