package org.collexio.domain;

import java.io.IOException;

public interface Growable {
    void addPhoto(ItemPhoto photo) throws IOException;
    String showPhotos();
}
