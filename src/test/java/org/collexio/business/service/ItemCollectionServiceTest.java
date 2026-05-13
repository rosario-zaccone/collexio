package org.collexio.business.service;

import org.collexio.business.domain.ItemCollection;
import org.collexio.persistence.dao.ItemCollectionDAO;
import org.collexio.persistence.entity.ItemCollectionEntity;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemCollectionServiceTest {

    private final ItemCollectionDAO dao = mock(ItemCollectionDAO.class);
    private final ItemCollectionService service = new ItemCollectionService(dao);

    @Test
    void addDelegatesAndMapsResult() throws SQLException, IOException {
        ItemCollection input = new ItemCollection("Console");
        ItemCollectionEntity saved = new ItemCollectionEntity(1L, "Console");
        when(dao.add(input.toEntity())).thenReturn(saved);

        ItemCollection result = service.add(input);

        assertEquals(saved.toString(), result.toEntity().toString());
    }

    @Test
    void getReturnsMappedCollectionWhenDaoFindsEntity() throws SQLException {
        ItemCollectionEntity entity = new ItemCollectionEntity(1L, "Console");
        when(dao.get(1L)).thenReturn(Optional.of(entity));

        ItemCollection result = service.get(1L);

        assertEquals(entity.toString(), result.toEntity().toString());
    }

    @Test
    void getThrowsWhenDaoReturnsEmpty() throws SQLException {
        when(dao.get(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.get(99L));
    }

    @Test
    void getAllMapsDaoEntities() throws SQLException {
        when(dao.getAll()).thenReturn(List.of(
                new ItemCollectionEntity(1L, "Console"),
                new ItemCollectionEntity(2L, "Books")
        ));

        List<ItemCollection> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("Console", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());
    }

    @Test
    void updateAndDeleteDelegateToDao() throws SQLException, IOException {
        ItemCollection collection = new ItemCollection(1L, "Console");

        service.update(collection);
        service.delete(1L);

        verify(dao).update(collection.toEntity());
        verify(dao).delete(1L);
    }
}
