package org.collexio.persistence;

import org.collexio.domain.Book;
import org.collexio.domain.Item;
import org.collexio.domain.ItemPhoto;
import org.collexio.domain.TechItem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/*
CREATE TABLE items(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    quantity INTEGER NOT NULL,
    description TEXT NOT NULL,
    type INTEGER NOT NULL, -- 0 for PLant, 1 for tech, 2 for book
    item_collection_id INTEGER,
    FOREIGN KEY(item_collection_id) REFERENCES item_collections(id) ON DELETE SET NULL ON UPDATE CASCADE
);
 */
public class DBItemDAO implements ItemDAO{
    private final Connection connection;

    private final static String insertSql = "INSERT INTO items(name, quantity, description, type, item_collection_id)"
            + "VALUES(?,?,?,?,?)";

    public DBItemDAO(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void add(Item item, int collectionId) throws SQLException {
        // -- 0 for PLant, 1 for tech, 2 for book
        int type = 0;
        if (item.getClass() == TechItem.class)
            type = 1;
        else if (item.getClass() == Book.class)
            type = 2;
        try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getQuantity());
            stmt.setString(3, item.getDescription());
            stmt.setInt(4, type);
            stmt.setInt(5, collectionId);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Item> get(int id) {
        return Optional.empty();
    }

    @Override
    public void update(Item item) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Item> getAll() throws SQLException {
        return List.of();
    }
}
