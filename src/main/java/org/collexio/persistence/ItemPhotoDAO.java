package org.collexio.persistence;

import org.collexio.domain.Item;
import org.collexio.domain.ItemPhoto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemPhotoDAO {
    void add(ItemPhoto photo);
    Optional<ItemPhoto> get(String id) throws SQLException;
    void update(ItemPhoto photo);
    void delete(String id);
    List<ItemPhoto> getAll() throws SQLException;
}
