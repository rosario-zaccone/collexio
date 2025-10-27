package org.collexio.domain;

import org.collexio.utilities.InfoGenerator;
import org.collexio.utilities.PFAFInfoGenerator;

import java.io.IOException;
public class Plant extends Item  { // add growable in future
    private final String scientificName;
    //private final Set<ItemPhoto> photos = new TreeSet<>();

    public Plant (String id, String name, int quantity, ItemPhoto photo, String scientificName) {
        super(id, name, quantity, photo);
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


    @Override
    public void generateDescription() {
        InfoGenerator generator = new PFAFInfoGenerator();
        setDescription(generator.generateDescription(getScientificName().toLowerCase()));
    }

    @Override
    public String toString() {
        return "Plant{} " + super.toString();
    }

    @Override
    public Plant copy()  {
        return new Plant(getId(), getName(), getQuantity(), getPhoto(), getScientificName());
    }
}
