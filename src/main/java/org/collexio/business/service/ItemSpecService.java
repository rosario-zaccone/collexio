package org.collexio.business.service;

import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.dao.ItemSpecDAO;
import org.collexio.persistence.model.ItemSpecEntity;
import org.collexio.utilities.InfoGenerator;
import org.collexio.utilities.InfoGeneratorFactory;
import org.collexio.utilities.PriceScraper;
import org.collexio.utilities.PriceScraperFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ItemSpecService {
    private final ItemSpecDAO dao;

    public ItemSpecService(ItemSpecDAO dao) {
        this.dao = dao;
    }

    public ItemSpec add(ItemSpec spec) throws SQLException {
        return ItemSpec.fromEntity(dao.add(spec.toEntity()));
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

    public ItemSpec getByItemId(Long id) throws SQLException {
        Optional<ItemSpecEntity> res = dao.getItemSpec(id);
        if (res.isEmpty())
            throw new NoSuchElementException("ItemSpec not found for id: " + id);
        return ItemSpec.fromEntity(res.get());
    }

    public List<ItemSpec> getAll() throws SQLException {
        List<ItemSpecEntity> fetched = dao.getAll();
        List<ItemSpec> res = new ArrayList<>();
        fetched.forEach(e -> res.add(ItemSpec.fromEntity(e)));
        return res;
    }


}
