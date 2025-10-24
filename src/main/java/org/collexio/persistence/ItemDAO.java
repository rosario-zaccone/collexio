package org.collexio.persistence;

import org.collexio.domain.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDAO {
    void add(Item item);
    Item get(String id);
    void update(Item item);
    void delete(String id);
    List<Item> getAll() throws SQLException;
}
