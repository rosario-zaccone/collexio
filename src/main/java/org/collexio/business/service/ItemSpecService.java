package org.collexio.business.service;

import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.dao.ItemSpecDAO;
import org.collexio.persistence.model.ItemSpecEntity;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ItemSpecService {
    private final ItemSpecDAO dao;

    public ItemSpecService(ItemSpecDAO dao) {
        this.dao = dao;
    }

    public void add(ItemSpec spec) throws SQLException {
        dao.add(spec.toEntity());
    }

    public ItemSpec get(Long id) throws SQLException {
        Optional<ItemSpecEntity> res = dao.get(id);
        if (res.isEmpty())
            throw new NoSuchElementException("ItemSpec not found for id: " + id);
        return ItemSpec.fromEntity(res.get());
    }

    public void update(ItemSpec spec) throws SQLException {
        dao.update(spec.toEntity());
    }

    public void delete(Long id) throws SQLException {
        dao.delete(id);
    }
}
