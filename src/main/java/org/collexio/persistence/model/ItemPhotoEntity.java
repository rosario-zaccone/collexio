package org.collexio.persistence.model;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Objects;

public class ItemPhotoEntity implements Comparable<ItemPhotoEntity> {
    private final Long id;
    private final Path path;
    private final LocalDate date;

    public ItemPhotoEntity(Long id, Path path, LocalDate date) {
        this.id = id;
        this.path = path;
        this.date = date;
    }


    public ItemPhotoEntity(Long id, Path path) {
        this(null, path, LocalDate.MIN);
    }

    public ItemPhotoEntity(Path path, LocalDate date) {
        this(null, path, date);
    }

    public Long getId() {
        return id;
    }


    public Path getPath() {
        return path;
    }

    public LocalDate getDate() {
        return date;
    }


    @Override
    public String toString() {
        return "ItemPhoto{" +
                "id='" + id + '\'' +
                ", path=" + path +
                ", date=" + date +
                '}';
    }

    @Override
    public int compareTo(@NotNull ItemPhotoEntity o) {
        return date.compareTo(o.getDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPhotoEntity itemPhoto = (ItemPhotoEntity) o;
        return Objects.equals(path, itemPhoto.path) && Objects.equals(date, itemPhoto.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, date);
    }
}
