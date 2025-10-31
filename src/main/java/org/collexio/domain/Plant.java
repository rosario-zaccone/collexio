package org.collexio.domain;

import org.collexio.utilities.InfoGenerator;
import org.collexio.utilities.PFAFInfoGenerator;

import java.io.IOException;
import java.util.Objects;

public class Plant extends Item  {
    private final String scientificName;

    public Plant (Long id, String name, int quantity, ItemPhoto photo, String scientificName) {
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
        return "Plant{" + "scientific_name=" + scientificName + ", " + super.toString() + "}";
    }

    public String toStringNoId() {
        return "Plant{" + "scientific_name=" + scientificName + ", " + super.toStringNoId() + "}";
    }

    @Override
    public Plant copy()  {
        Plant item =  new Plant(getId(), getName(), getQuantity(), getPhoto(), getScientificName());
        item.setDescription(getDescription());
        item.getTransactions().forEach(item::addTransaction);
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Plant plant = (Plant) o;
        return Objects.equals(scientificName, plant.scientificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), scientificName);
    }
}
