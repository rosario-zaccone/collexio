package org.collexio.domain;

import org.collexio.utilities.InfoGenerator;
import org.collexio.utilities.PFAFInfoGenerator;

import java.util.ArrayList;
import java.util.List;

public class Plant extends Item implements Growable {
    private final String scientificName;
    private final List<String> photos = new ArrayList<>();

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

    public List<String> getPhotos() {
        return List.copyOf(photos); //immutable
    }

    @Override
    public String description() {
        InfoGenerator generator = new PFAFInfoGenerator();
        return generator.generateDescription(getScientificName().toLowerCase());
    }

    @Override
    public void addPhoto(String photo) {
        photos.add(photo);
        // add to the cloud also
    }

    @Override
    public String showPhotos() {
        return "TODO";
    }
}
