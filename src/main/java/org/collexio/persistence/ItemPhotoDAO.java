package org.collexio.persistence;

import org.collexio.domain.Item;
import org.collexio.domain.ItemPhoto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemPhotoDAO {
    void add(ItemPhoto photo, int itemId) throws SQLException;
    Optional<ItemPhoto> get(int id) throws SQLException;
    Optional<ItemPhoto> getByItemId(int itemId) throws SQLException;
    void update(ItemPhoto photo) throws SQLException;
    //void delete(String id);
    List<ItemPhoto> getAll() throws SQLException;
}
