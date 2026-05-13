package org.collexio.business.service;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemPhoto;
import org.collexio.business.domain.ItemSpec;
import org.collexio.business.domain.Transaction;
import org.collexio.persistence.entity.ItemStatus;
import org.collexio.persistence.entity.ItemType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemOrchestratorTest {

    private final ItemService itemService = mock(ItemService.class);
    private final ItemPhotoService photoService = mock(ItemPhotoService.class);
    private final TransactionService transactionService = mock(TransactionService.class);
    private final ItemSpecService specService = mock(ItemSpecService.class);
    private final ItemOrchestrator service =
            new ItemOrchestrator(itemService, photoService, transactionService, specService);

    @Test
    void addWithPhotoWrapsServicesInTransaction() throws SQLException, IOException {
        Connection connection = mock(Connection.class);
        Item input = new Item(ItemStatus.GOOD, spec(null));
        Item savedItem = new Item(1L, ItemStatus.GOOD, null, spec(2L));
        ItemPhoto inputPhoto = new ItemPhoto(Path.of("source.png"), LocalDate.of(2024, 1, 1));
        ItemPhoto savedPhoto = new ItemPhoto(3L, Path.of("images/1.png"), LocalDate.of(2024, 1, 1));
        when(itemService.getConnection()).thenReturn(connection);
        when(itemService.add(input, 4L)).thenReturn(savedItem);
        when(photoService.add(inputPhoto, 1L)).thenReturn(savedPhoto);

        Item result = service.addWithPhoto(input, 4L, inputPhoto);

        var order = inOrder(connection, itemService, photoService);
        order.verify(connection).setAutoCommit(false);
        order.verify(itemService).add(input, 4L);
        order.verify(photoService).add(inputPhoto, 1L);
        order.verify(connection).setAutoCommit(true);
        assertEquals(savedPhoto, result.getPhoto());
    }

    @Test
    void addWithPhotoRollsBackWhenPhotoServiceFails() throws SQLException, IOException {
        Connection connection = mock(Connection.class);
        Item input = new Item(ItemStatus.GOOD, spec(null));
        Item savedItem = new Item(1L, ItemStatus.GOOD, null, spec(2L));
        ItemPhoto inputPhoto = new ItemPhoto(Path.of("source.png"), LocalDate.of(2024, 1, 1));
        IOException failure = new IOException("failed");
        when(itemService.getConnection()).thenReturn(connection);
        when(itemService.add(input, 4L)).thenReturn(savedItem);
        when(photoService.add(inputPhoto, 1L)).thenThrow(failure);

        assertThrows(IOException.class, () -> service.addWithPhoto(input, 4L, inputPhoto));

        verify(connection).rollback();
        verify(connection).setAutoCommit(true);
    }

    @Test
    void updateWithPhotoWrapsServicesInTransaction() throws SQLException, IOException {
        Connection connection = mock(Connection.class);
        Item item = new Item(1L, ItemStatus.AVERAGE, null, spec(2L));
        ItemPhoto inputPhoto = new ItemPhoto(3L, Path.of("source.png"), LocalDate.of(2024, 1, 1));
        ItemPhoto savedPhoto = new ItemPhoto(3L, Path.of("images/1.png"), LocalDate.of(2024, 1, 1));
        when(itemService.getConnection()).thenReturn(connection);
        when(photoService.update(inputPhoto, 1L)).thenReturn(savedPhoto);

        Item result = service.updateWithPhoto(item, 4L, inputPhoto);

        var order = inOrder(connection, itemService, photoService);
        order.verify(connection).setAutoCommit(false);
        order.verify(itemService).update(item, 4L);
        order.verify(photoService).update(inputPhoto, 1L);
        order.verify(connection).setAutoCommit(true);
        assertEquals(savedPhoto, result.getPhoto());
    }

    @Test
    void getFullItemLoadsPhotoSpecAndTransactions() throws SQLException {
        Item item = new Item(1L, ItemStatus.GOOD, null, null);
        ItemPhoto photo = new ItemPhoto(2L, Path.of("images/1.png"), LocalDate.of(2024, 1, 1));
        ItemSpec spec = spec(3L);
        Transaction expense = new Transaction(4L, 10, false, LocalDate.of(2024, 1, 2));
        Transaction income = new Transaction(5L, 15, true, LocalDate.of(2024, 1, 3));
        when(itemService.get(1L)).thenReturn(item);
        when(photoService.getByItemId(1L)).thenReturn(photo);
        when(specService.getByItemId(1L)).thenReturn(spec);
        when(transactionService.getByItemId(1L)).thenReturn(List.of(expense, income));

        Item result = service.getFullItem(1L);

        assertEquals(photo, result.getPhoto());
        assertEquals(spec, result.getSpec());
        assertEquals(List.of(expense, income), result.getTransactions());
    }

    @Test
    void getAllFullItemsLoadsDetailsForEveryItem() throws SQLException {
        Item first = new Item(1L, ItemStatus.GOOD, null, null);
        Item second = new Item(2L, ItemStatus.BAD, null, null);
        when(itemService.getAll()).thenReturn(List.of(first, second));
        when(itemService.get(1L)).thenReturn(first);
        when(itemService.get(2L)).thenReturn(second);
        when(photoService.getByItemId(1L)).thenReturn(new ItemPhoto(1L, Path.of("images/1.png"), LocalDate.MIN));
        when(photoService.getByItemId(2L)).thenReturn(new ItemPhoto(2L, Path.of("images/2.png"), LocalDate.MIN));
        when(specService.getByItemId(1L)).thenReturn(spec(1L));
        when(specService.getByItemId(2L)).thenReturn(spec(2L));
        when(transactionService.getByItemId(1L)).thenReturn(List.of());
        when(transactionService.getByItemId(2L)).thenReturn(List.of());

        List<Item> result = service.getAllFullItems();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    private ItemSpec spec(Long id) {
        return new ItemSpec(id, ItemType.TECHITEM, "Nintendo DS", "Console");
    }
}
