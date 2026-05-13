package org.collexio.business.domain;

import org.collexio.persistence.entity.ItemPhotoEntity;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemPhotoTest {

    @Test
    void rejectsNonPositiveId() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPhoto(0L, Path.of("photo.png"), LocalDate.now()));
    }

    @Test
    void comparesByDate() {
        ItemPhoto older = new ItemPhoto(Path.of("a.png"), LocalDate.of(2024, 1, 1));
        ItemPhoto newer = new ItemPhoto(Path.of("b.png"), LocalDate.of(2024, 1, 2));

        assertTrue(older.compareTo(newer) < 0);
    }

    @Test
    void equalityIgnoresId() {
        ItemPhoto first = new ItemPhoto(1L, Path.of("a.png"), LocalDate.of(2024, 1, 1));
        ItemPhoto second = new ItemPhoto(2L, Path.of("a.png"), LocalDate.of(2024, 1, 1));

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void convertsToAndFromEntity() {
        ItemPhotoEntity entity = new ItemPhotoEntity(1L, Path.of("a.png"), LocalDate.of(2024, 1, 1));

        ItemPhoto photo = ItemPhoto.fromEntity(entity);

        assertEquals(entity.toString(), photo.toEntity().toString());
    }
}
