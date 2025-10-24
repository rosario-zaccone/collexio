package org.collexio.domain;

import org.collexio.utilities.Utilities;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class ItemPhoto implements Comparable<ItemPhoto> {
    private final String id;
    private final Path path;
    private final LocalDateTime timestamp;
    private final PhotoType type;

    public ItemPhoto(String id, Path path, LocalDateTime timestamp, PhotoType type) {
        if (!Utilities.validateId("P-", id))
            throw new IllegalArgumentException("Id must be in the format I-###, where ### is a natural number");
        this.id = id;
        this.path = path;
        this.timestamp = timestamp;
        this.type = type;
    }


    public ItemPhoto(String id, Path path, PhotoType type) {
        if (!Utilities.validateId("P-", id))
            throw new IllegalArgumentException("Id must be in the format I-###, where ### is a natural number");
        this.id = id;
        this.path = path;
        this.type = type;
        this.timestamp = LocalDateTime.MIN;
    }

    public String getId() {
        return id;
    }

    public PhotoType getType() {
        return type;
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
                ", type=" + type +
                '}';
    }

    @Override
    public int compareTo(@NotNull ItemPhoto o) {
        return timestamp.compareTo(o.getTimestamp());
    }
}
