package org.collexio.domain;

import org.collexio.utilities.InfoGenerator;
import org.collexio.utilities.PFAFInfoGenerator;

import java.io.IOException;
public class Plant extends Item  {
    private final String scientificName;

    public Plant (int id, String name, int quantity, ItemPhoto photo, String scientificName) {
        super(id, name, quantity, photo);
        if (!validateScientificName(scientificName))
            throw new IllegalArgumentException("Invalid scientific name");
        this.scientificName = scientificName;
    }

    public Plant (String name, int quantity, ItemPhoto photo, String scientificName) {
        super(name, quantity, photo);
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
