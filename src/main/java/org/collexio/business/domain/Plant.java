package org.collexio.business.domain;

import org.collexio.utilities.InfoGenerator;
import org.collexio.utilities.PFAFInfoGenerator;

import java.util.Objects;

public class Plant extends Item  {
    private final String scientificName;

    public Plant (Long id, String name, int quantity, ItemPhoto photo, String description, String scientificName) {
        super(id, name, quantity, photo, description);
        if (!validateScientificName(scientificName))
            throw new IllegalArgumentException("Invalid scientific name");
        this.scientificName = scientificName;
    }

    public Plant(Long id, String name, int quantity, ItemPhoto photo, String scientificName) {
        this(id, name, quantity, photo, "no description", scientificName);
    }

    public Plant (String name, int quantity, ItemPhoto photo, String description, String scientificName) {
        this(null, name, quantity, photo, description, scientificName);
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
        Plant item =  new Plant(getId(), getName(), getQuantity(), getPhoto(), getDescription(), scientificName);
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
