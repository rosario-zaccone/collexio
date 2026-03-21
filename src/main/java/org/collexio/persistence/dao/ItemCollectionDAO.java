package org.collexio.persistence.dao;


import org.collexio.persistence.model.ItemCollectionEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemCollectionDAO {
    ItemCollectionEntity add(ItemCollectionEntity collection) throws SQLException;
    Optional<ItemCollectionEntity> get(Long id) throws SQLException;
    void update(ItemCollectionEntity collection) throws SQLException;
    void delete(Long id) throws SQLException;
    List<ItemCollectionEntity> getAll() throws SQLException;
}
