package org.collexio.persistence.entity;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemPhotoEntityTest {

    @Test
    void storesValues() {
        ItemPhotoEntity photo = new ItemPhotoEntity(1L, Path.of("photo.png"), LocalDate.of(2024, 1, 1));

        assertEquals(1L, photo.getId());
        assertEquals(Path.of("photo.png"), photo.getPath());
        assertEquals(LocalDate.of(2024, 1, 1), photo.getDate());
    }

    @Test
    void comparesByDate() {
        ItemPhotoEntity older = new ItemPhotoEntity(Path.of("a.png"), LocalDate.of(2024, 1, 1));
        ItemPhotoEntity newer = new ItemPhotoEntity(Path.of("b.png"), LocalDate.of(2024, 1, 2));

        assertTrue(older.compareTo(newer) < 0);
    }

    @Test
    void equalityIgnoresId() {
        assertEquals(
                new ItemPhotoEntity(1L, Path.of("a.png"), LocalDate.of(2024, 1, 1)),
                new ItemPhotoEntity(2L, Path.of("a.png"), LocalDate.of(2024, 1, 1))
        );
    }
}
