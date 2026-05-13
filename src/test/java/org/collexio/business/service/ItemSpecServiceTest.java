package org.collexio.business.service;

import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.dao.ItemSpecDAO;
import org.collexio.persistence.entity.ItemSpecEntity;
import org.collexio.persistence.entity.ItemType;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemSpecServiceTest {

    private final ItemSpecDAO dao = mock(ItemSpecDAO.class);
    private final ItemSpecService service = new ItemSpecService(dao);

    @Test
    void addDelegatesAndMapsResult() throws SQLException {
        ItemSpec input = new ItemSpec(ItemType.TECHITEM, "Nintendo DS", "Console");
        ItemSpecEntity saved = new ItemSpecEntity(1L, ItemType.TECHITEM, "Nintendo DS", "Console");
        when(dao.add(input.toEntity())).thenReturn(saved);

        ItemSpec result = service.add(input);

        assertEquals(saved.toString(), result.toEntity().toString());
    }

    @Test
    void getReturnsMappedSpecWhenDaoFindsEntity() throws SQLException {
        ItemSpecEntity entity = new ItemSpecEntity(1L, ItemType.BOOK, "Dragon Ball", "Manga");
        when(dao.get(1L)).thenReturn(Optional.of(entity));

        ItemSpec result = service.get(1L);

        assertEquals(entity.toString(), result.toEntity().toString());
    }

    @Test
    void getThrowsWhenDaoReturnsEmpty() throws SQLException {
        when(dao.get(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.get(99L));
    }

    @Test
    void getByItemIdReturnsMappedSpecWhenDaoFindsEntity() throws SQLException {
        ItemSpecEntity entity = new ItemSpecEntity(2L, ItemType.TECHITEM, "PSP", "Console");
        when(dao.getItemSpec(7L)).thenReturn(Optional.of(entity));

        ItemSpec result = service.getByItemId(7L);

        assertEquals(entity.toString(), result.toEntity().toString());
    }

    @Test
    void getByItemIdThrowsWhenDaoReturnsEmpty() throws SQLException {
        when(dao.getItemSpec(7L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getByItemId(7L));
    }

    @Test
    void getAllMapsDaoEntities() throws SQLException {
        when(dao.getAll()).thenReturn(List.of(
                new ItemSpecEntity(1L, ItemType.TECHITEM, "Nintendo DS", "Console"),
                new ItemSpecEntity(2L, ItemType.BOOK, "Dragon Ball", "Manga")
        ));

        List<ItemSpec> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("Nintendo DS", result.get(0).getName());
        assertEquals("Dragon Ball", result.get(1).getName());
    }

    @Test
    void updateAndDeleteDelegateToDao() throws SQLException {
        ItemSpec spec = new ItemSpec(1L, ItemType.TECHITEM, "Nintendo DS", "Console");

        service.update(spec);
        service.delete(1L);

        verify(dao).update(spec.toEntity());
        verify(dao).delete(1L);
    }
}
