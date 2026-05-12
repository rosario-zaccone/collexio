package org.collexio.business.service;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.dao.ItemDAO;
import org.collexio.persistence.entity.ItemEntity;
import org.collexio.persistence.entity.ItemStatus;
import org.collexio.persistence.entity.ItemType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemServiceTest {

    private final ItemDAO dao = mock(ItemDAO.class);
    private final ItemService service = new ItemService(dao);

    @Test
    void addDelegatesAndMapsResult() throws SQLException, IOException {
        Item input = new Item(ItemStatus.GOOD, spec(null));
        ItemEntity saved = new ItemEntity(1L, ItemStatus.GOOD, spec(2L).toEntity());
        when(dao.add(input.toEntity(), 3L)).thenReturn(saved);

        Item result = service.add(input, 3L);

        assertEquals(saved.toString(), result.toEntity().toString());
    }

    @Test
    void getReturnsMappedItemWhenDaoFindsEntity() throws SQLException {
        ItemEntity entity = new ItemEntity(1L, ItemStatus.AVERAGE, spec(2L).toEntity());
        when(dao.get(1L)).thenReturn(Optional.of(entity));

        Item result = service.get(1L);

        assertEquals(entity.toString(), result.toEntity().toString());
    }

    @Test
    void getThrowsWhenDaoReturnsEmpty() throws SQLException {
        when(dao.get(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.get(99L));
    }

    @Test
    void mapsListQueriesFromDao() throws SQLException {
        ItemEntity first = new ItemEntity(1L, ItemStatus.GOOD, spec(1L).toEntity());
        ItemEntity second = new ItemEntity(2L, ItemStatus.BAD, spec(2L).toEntity());
        when(dao.getByCollectionId(7L)).thenReturn(List.of(first, second));
        when(dao.getAll()).thenReturn(List.of(second));

        assertEquals(2, service.getByCollectionId(7L).size());
        assertEquals(second.toString(), service.getAll().get(0).toEntity().toString());
    }

    @Test
    void updateDeleteAndGetCollectionIdDelegateToDao() throws SQLException, IOException {
        Item item = new Item(1L, ItemStatus.AVERAGE, null, spec(2L));
        when(dao.getCollectionId(1L)).thenReturn(Optional.of(4L));

        service.update(item, 4L);
        service.delete(1L);

        verify(dao).update(item.toEntity(), 4L);
        verify(dao).delete(1L);
        assertEquals(Optional.of(4L), service.getCollectionId(1L));
    }

    @Test
    void isAvailableUsesDomainState() {
        assertTrue(service.isAvailable(new Item(ItemStatus.GOOD, spec(1L))));
        Item unavailable = new Item(ItemStatus.GOOD, spec(1L));
        unavailable.addTransaction(new org.collexio.business.domain.Transaction(1, true, java.time.LocalDate.MIN));
        assertFalse(service.isAvailable(unavailable));
    }

    private ItemSpec spec(Long id) {
        return new ItemSpec(id, ItemType.TECHITEM, "Nintendo DS", "Console");
    }
}
