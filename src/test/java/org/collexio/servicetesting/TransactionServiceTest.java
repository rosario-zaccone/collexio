package org.collexio.servicetesting;

import org.collexio.business.domain.Transaction;
import org.collexio.business.service.TransactionService;
import org.collexio.persistence.dao.TransactionDAO;
import org.collexio.persistence.entity.TransactionEntity;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private final TransactionDAO dao = mock(TransactionDAO.class);
    private final TransactionService service = new TransactionService(dao);

    @Test
    void addDelegatesAndMapsResult() throws SQLException {
        Transaction input = new Transaction(10, true, LocalDate.of(2024, 1, 1));
        TransactionEntity saved = new TransactionEntity(1L, 10, true, LocalDate.of(2024, 1, 1));
        when(dao.add(input.toEntity(), 2L)).thenReturn(saved);

        Transaction result = service.add(input, 2L);

        assertEquals(saved.toString(), result.toEntity().toString());
    }

    @Test
    void updateDeleteAndGetItemIdDelegate() throws SQLException {
        Transaction transaction = new Transaction(1L, 10, true, LocalDate.of(2024, 1, 1));
        when(dao.getItemId(1L)).thenReturn(7L);

        service.update(transaction);
        service.delete(1L);

        verify(dao).update(transaction.toEntity());
        verify(dao).delete(1L);
        assertEquals(7L, service.getItemId(1L));
    }

    @Test
    void mapsListsFromDao() throws SQLException {
        TransactionEntity first = new TransactionEntity(1L, 10, true, LocalDate.of(2024, 1, 1));
        TransactionEntity second = new TransactionEntity(2L, 20, false, LocalDate.of(2024, 1, 2));
        when(dao.getAll()).thenReturn(List.of(first, second));
        when(dao.getByItemId(9L)).thenReturn(List.of(second));

        assertEquals(2, service.getAll().size());
        assertEquals(second.toString(), service.getByItemId(9L).get(0).toEntity().toString());
    }
}
