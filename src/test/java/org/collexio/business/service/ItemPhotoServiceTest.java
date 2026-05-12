package org.collexio.business.service;

import org.collexio.AppConfig;
import org.collexio.business.domain.ItemPhoto;
import org.collexio.persistence.dao.ItemPhotoDAO;
import org.collexio.persistence.entity.ItemPhotoEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemPhotoServiceTest {

    @TempDir
    Path tempDir;

    private String originalUserHome;
    private Path source;
    private ItemPhotoDAO dao;
    private ItemPhotoService service;

    @BeforeEach
    void setUp() throws IOException {
        originalUserHome = System.getProperty("user.home");
        System.setProperty("user.home", tempDir.toString());
        Files.createDirectories(AppConfig.getImagesDir());
        source = tempDir.resolve("source.png");
        Files.writeString(source, "image");
        dao = mock(ItemPhotoDAO.class);
        service = new ItemPhotoService(dao);
    }

    @AfterEach
    void tearDown() {
        System.setProperty("user.home", originalUserHome);
    }

    @Test
    void addCopiesFileAndPersistsPhotoWithTargetPath() throws SQLException, IOException {
        ItemPhoto input = new ItemPhoto(source, LocalDate.of(2024, 1, 1));
        Path target = AppConfig.getImagesDir().resolve("7.png");
        ItemPhotoEntity saved = new ItemPhotoEntity(1L, target, input.getDate());
        when(dao.add(any(ItemPhotoEntity.class), org.mockito.ArgumentMatchers.eq(7L))).thenReturn(saved);

        ItemPhoto result = service.add(input, 7L);

        ArgumentCaptor<ItemPhotoEntity> captor = ArgumentCaptor.forClass(ItemPhotoEntity.class);
        verify(dao).add(captor.capture(), org.mockito.ArgumentMatchers.eq(7L));
        assertEquals(target, captor.getValue().getPath());
        assertTrue(Files.exists(target));
        assertEquals(saved.toString(), result.toEntity().toString());
    }

    @Test
    void addDeletesCopiedFileWhenDaoFails() throws SQLException, IOException {
        ItemPhoto input = new ItemPhoto(source, LocalDate.of(2024, 1, 1));
        Path target = AppConfig.getImagesDir().resolve("7.png");
        when(dao.add(any(ItemPhotoEntity.class), org.mockito.ArgumentMatchers.eq(7L)))
                .thenThrow(new SQLException("failed"));

        assertThrows(SQLException.class, () -> service.add(input, 7L));
        assertTrue(Files.notExists(target));
    }

    @Test
    void getByItemIdReturnsMappedPhotoWhenDaoFindsEntity() throws SQLException {
        ItemPhotoEntity entity = new ItemPhotoEntity(1L, Path.of("images/1.png"), LocalDate.of(2024, 1, 1));
        when(dao.getByItemId(7L)).thenReturn(Optional.of(entity));

        ItemPhoto result = service.getByItemId(7L);

        assertEquals(entity.toString(), result.toEntity().toString());
    }

    @Test
    void getByItemIdThrowsWhenDaoReturnsEmpty() throws SQLException {
        when(dao.getByItemId(7L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getByItemId(7L));
    }

    @Test
    void updateCopiesFileAndDelegatesUpdatedPhoto() throws SQLException, IOException {
        ItemPhoto input = new ItemPhoto(1L, source, LocalDate.of(2024, 1, 1));
        Path target = AppConfig.getImagesDir().resolve("7.png");

        ItemPhoto result = service.update(input, 7L);

        ArgumentCaptor<ItemPhotoEntity> captor = ArgumentCaptor.forClass(ItemPhotoEntity.class);
        verify(dao).update(captor.capture());
        assertEquals(target, captor.getValue().getPath());
        assertEquals(target, result.getPath());
        assertTrue(Files.exists(target));
    }

    @Test
    void deleteByItemIdDelegatesToDao() throws SQLException {
        service.deleteByItemId(7L);

        verify(dao).deleteByItemId(7L);
    }
}
