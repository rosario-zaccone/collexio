package org.collexio.persistence.dao;

import org.collexio.persistence.entity.ItemSpecEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemSpecDAO {
    ItemSpecEntity add(ItemSpecEntity spec) throws SQLException;
    Optional<ItemSpecEntity> get(Long id) throws SQLException;
    void update(ItemSpecEntity spec) throws SQLException;
    void delete(Long id) throws SQLException;
    List<ItemSpecEntity> getAll() throws SQLException;
    Optional<ItemSpecEntity> getItemSpec(Long id) throws SQLException;
}
