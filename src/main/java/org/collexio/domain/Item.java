package org.collexio.domain;


import org.collexio.utilities.Utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class Item {
    private final String id; //I-001
    private String name;
    private int quantity;
    private ItemPhoto photo;
    private String description;

    public Item(String id, String name, int quantity, ItemPhoto photo) throws IOException {
        if (!Utilities.validateId("I-", id))
            throw new IllegalArgumentException("Id must be in the format I-###, where ### is a natural number");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        if (name.isEmpty() || !name.matches("[\\p{L}\\p{N} ]+"))
            throw new IllegalArgumentException("Name can't be empty and can contain only letters, number and spaces");
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        uploadPhoto(photo);
        this.photo = photo;
        description = "NO DESCRIPTION";
    }


    public String getId() {
        return id;
    }

    public void setName(String name) {
        if (name.isEmpty() || !name.matches("[\\p{L}\\p{N} ]+"))
            throw new IllegalArgumentException("Name can't be empty and can contain only letters, number and spaces");
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract void generateDescription();

    public ItemPhoto getPhoto() {
        return photo;
    }

    private void uploadPhoto(ItemPhoto photo) throws IOException {
        try (InputStream is = Files.newInputStream(photo.getPath())) {
            BufferedImage originalImage = ImageIO.read(is);
            BufferedImage resizedImage = new BufferedImage(100, 100, originalImage.getType());
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, 100, 100, null);
            g.dispose();
            String fileName = photo.getPath().getFileName().toString();
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex == -1) {
                throw new IOException("File extension not found");
            }
            String ext = fileName.substring(dotIndex + 1).toLowerCase();
            ImageIO.write(resizedImage, ext, Paths.get("images/thumbnails/" + id + "." + ext).toFile());
        }
    }

    public void setPhoto(ItemPhoto photo) throws IOException {
        uploadPhoto(photo);
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item item)) return false;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", photo=" + photo +
                '}';
    }
}
