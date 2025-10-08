package org.collexio;

public class Plant extends LivingItem {
    private final String scientificName;

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

    @Override
    public String description() {
        InfoGenerator generator = new PFAFInfoGenerator();
        return generator.generateDescription(scientificName.toLowerCase());
    }

    @Override
    public void addPhoto(String path) {
    // TODO
    }

    @Override
    public void showPhotos() {
    // TODO
    }
}
