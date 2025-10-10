package org.collexio.domain;

import java.time.LocalDateTime;

public class Photo {
    private final String path;
    private final LocalDateTime timestamp;

    public Photo(String path, LocalDateTime timestamp) {
        this.path = path;
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
