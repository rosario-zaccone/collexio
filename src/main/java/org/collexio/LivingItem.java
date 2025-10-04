package org.collexio;

public abstract class LivingItem extends Item {
    public LivingItem(String id, String name, int quantity) {
        super(id, name, quantity);
    }
    public abstract void addPhoto(String path);
    public abstract void showPhotos();

}
