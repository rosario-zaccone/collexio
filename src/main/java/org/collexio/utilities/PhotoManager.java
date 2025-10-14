package org.collexio.utilities;

import org.collexio.domain.Growable;
import org.collexio.domain.Item;
import org.collexio.domain.ItemPhoto;

import java.io.IOException;
import java.util.Set;

public interface PhotoManager {
    void addPhoto(String itemId, ItemPhoto photo) throws IOException;
    Set<ItemPhoto> getPhotos(String itemId) throws IOException;
}
