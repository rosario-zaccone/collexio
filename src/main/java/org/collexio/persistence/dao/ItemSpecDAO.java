package org.collexio.persistence.dao;

import org.collexio.persistence.model.ItemEntity;
import org.collexio.persistence.model.ItemPhotoEntity;
import org.collexio.persistence.model.ItemSpecEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemSpecDAO {
    void add(ItemSpecEntity spec) throws SQLException;
    Optional<ItemSpecEntity> get(Long id) throws SQLException;
    void update(ItemSpecEntity spec) throws SQLException;
    void delete(Long id) throws SQLException;
    List<ItemSpecEntity> getAll() throws SQLException;
}
