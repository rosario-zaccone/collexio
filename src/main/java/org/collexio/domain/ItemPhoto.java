package org.collexio.domain;

import org.collexio.utilities.Utilities;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;

public class ItemPhoto implements Comparable<ItemPhoto> {
    private final Long id;
    private final Path path;
    private final LocalDateTime timestamp;

    public ItemPhoto(Long id, Path path, LocalDateTime timestamp) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("Id must be positive");
        this.id = id;
        this.path = path;
        this.timestamp = timestamp;
    }


    public ItemPhoto(Long id, Path path) {
        this(null, path, LocalDateTime.MIN);
    }

    public ItemPhoto(Path path, LocalDateTime timestamp) {
        this(null, path, timestamp);
    }

    public Long getId() {
        return id;
    }


    public Path getPath() {
        return path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return "ItemPhoto{" +
                "id='" + id + '\'' +
                ", path=" + path +
                ", timestamp=" + timestamp +
                '}';
    }

    public String toStringNoId() {
        return "ItemPhoto{" +
                "path=" + path +
                ", timestamp=" + timestamp +
                '}';
    }
    @Override
    public int compareTo(@NotNull ItemPhoto o) {
        return timestamp.compareTo(o.getTimestamp());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPhoto itemPhoto = (ItemPhoto) o;
        return Objects.equals(path, itemPhoto.path) && Objects.equals(timestamp, itemPhoto.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, timestamp);
    }
}
