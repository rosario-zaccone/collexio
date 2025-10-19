package org.collexio.domain;

import org.collexio.utilities.InfoGenerator;
import org.collexio.utilities.PFAFInfoGenerator;
import org.collexio.utilities.PhotoManager;
import org.collexio.utilities.SystemPhotoManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Plant extends Item implements Growable {
    private final String scientificName;
    private final Set<ItemPhoto> photos = new TreeSet<>();

    public Plant (String id, String name, int quantity, String scientificName) {
        super(id, name, quantity);
        if (!validateScientificName(scientificName))
            throw new IllegalArgumentException("Invalid scientific name");
        this.scientificName = scientificName;
    }

    private boolean validateScientificName(String value) {
        return value.split("_").length == 2;
    }

    public String getScientificName() {
        return scientificName;
    }

    public Set<ItemPhoto> getPhotos() {
        return Set.copyOf(photos); //immutable
    }

    @Override
    public void generateDescription() {
        InfoGenerator generator = new PFAFInfoGenerator();
        setDescription(generator.generateDescription(getScientificName().toLowerCase()));
    }

    @Override
    public void addPhoto(ItemPhoto photo) throws IOException {
        photos.add(photo);
        PhotoManager manager = new SystemPhotoManager("images/growable/");
        manager.addPhoto(getId(), photo);
    }

    @Override
    public String showPhotos() {
        return "TODO (pdf document with all photos)";
    }

    @Override
    public String toString() {
        return "Plant{} " + super.toString();
    }
}
