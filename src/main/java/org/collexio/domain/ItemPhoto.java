package org.collexio.domain;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class ItemPhoto implements Comparable<ItemPhoto> {
    private final Path path;
    private final LocalDateTime timestamp;

    public ItemPhoto(Path path, LocalDateTime timestamp) {
        this.path = path;
        this.timestamp = timestamp;
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
                "path=" + path +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int compareTo(@NotNull ItemPhoto o) {
        return timestamp.compareTo(o.getTimestamp());
    }
}
