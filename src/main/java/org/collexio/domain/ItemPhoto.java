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

public class ItemPhoto implements Comparable<ItemPhoto> {
    private final String id;
    private final Path path;
    private final LocalDateTime timestamp;

    public ItemPhoto(String id, Path path, LocalDateTime timestamp) {
        if (!Utilities.validateId("P-", id))
            throw new IllegalArgumentException("Id must be in the format I-###, where ### is a natural number");
        this.id = id;
        this.path = path;
        this.timestamp = timestamp;
    }


    public ItemPhoto(String id, Path path) {
        if (!Utilities.validateId("P-", id))
            throw new IllegalArgumentException("Id must be in the format I-###, where ### is a natural number");
        this.id = id;
        this.path = path;
        this.timestamp = LocalDateTime.MIN;
    }

    public String getId() {
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

    @Override
    public int compareTo(@NotNull ItemPhoto o) {
        return timestamp.compareTo(o.getTimestamp());
    }
}
