package org.collexio.persistence;

import org.collexio.domain.Item;
import org.collexio.domain.ItemPhoto;
import org.collexio.domain.Plant;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SqliteItemDAO implements ItemDAO {
    private final Connection connection;

    private static final String selectAllSql = "SELECT * FROM item";

    public SqliteItemDAO(Connection connection) { // DI, life of connection managed externally
        this.connection = connection;
    }

    @Override
    public void add(Item item) {

    }

    @Override
    public Item get(String id) {
        return null;
    }

    @Override
    public void update(Item item) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    //id, name, description, quantity, type, collection_id, second_name
    public List<Item> getAll() throws SQLException {
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(selectAllSql)) {
            while (rs.next()) {
                int rawId = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int quantity = rs.getInt("quantity");
                int type = rs.getInt("type"); // 0 for plants, 1 for tech, 2 for book
                int rawCollectionId = rs.getInt("collection_id");
                String secondName = rs.getString("second_name");
                String id = "I-" + rawId; String collectionId = "C-" + rawCollectionId;
                ItemPhoto photo = null; // da sistemare
                var result = switch(type) {
                    case 0 -> 0;
                    case 1 -> 1; // conviene fare prima itemphotodao
                    case 2 -> 2;
                    default -> throw new SQLException(); //da siustemare
                };

            }
        }
        return null;
    }
}
