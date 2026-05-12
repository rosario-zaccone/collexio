package org.collexio.business.service;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemCollection;
import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.entity.ItemStatus;
import org.collexio.persistence.entity.ItemType;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemCollectionOrchestratorTest {

    private final ItemService itemService = mock(ItemService.class);
    private final ItemCollectionService collectionService = mock(ItemCollectionService.class);
    private final ItemCollectionOrchestrator service =
            new ItemCollectionOrchestrator(itemService, collectionService);

    @Test
    void getFullCollectionLoadsItemsForCollection() throws SQLException {
        ItemCollection collection = new ItemCollection(1L, "Console");
        Item first = new Item(1L, ItemStatus.GOOD, null, spec(1L));
        Item second = new Item(2L, ItemStatus.AVERAGE, null, spec(2L));
        when(collectionService.get(1L)).thenReturn(collection);
        when(itemService.getByCollectionId(1L)).thenReturn(List.of(first, second));

        ItemCollection result = service.getFullCollection(1L);

        assertEquals(2, result.getData().size());
        assertEquals(List.of(first, second), result.getData());
    }

    @Test
    void getAllFullCollectionsLoadsItemsForEveryCollection() throws SQLException {
        ItemCollection first = new ItemCollection(1L, "Console");
        ItemCollection second = new ItemCollection(2L, "Books");
        when(collectionService.getAll()).thenReturn(List.of(first, second));
        when(collectionService.get(1L)).thenReturn(first);
        when(collectionService.get(2L)).thenReturn(second);
        when(itemService.getByCollectionId(1L)).thenReturn(List.of(new Item(1L, ItemStatus.GOOD, null, spec(1L))));
        when(itemService.getByCollectionId(2L)).thenReturn(List.of(new Item(2L, ItemStatus.BAD, null, spec(2L))));

        List<ItemCollection> result = service.getAllFullCollections();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getData().size());
        assertEquals(1, result.get(1).getData().size());
    }

    private ItemSpec spec(Long id) {
        return new ItemSpec(id, ItemType.TECHITEM, "Nintendo DS", "Console");
    }
}
