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

    public Item(String id, String name, int quantity) {
        if (!Utilities.validateId("I-", id))
            throw new IllegalArgumentException("Id must be in the format I-###, where ### is a natural number");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive");
        if (name.isEmpty())
            throw new IllegalArgumentException("Name can't be empty");
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        if (name.isEmpty())
            throw new IllegalArgumentException("Name can't be empty");
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

    public abstract String description();

    public ItemPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(ItemPhoto photo, int width, int height) {
        if (width < 20)
            throw new IllegalArgumentException("Width must be > 20");
        if (height < 20)
            throw new IllegalArgumentException("Height must be > 20");
        try (InputStream is = Files.newInputStream(photo.getPath())) {
            BufferedImage originalImage = ImageIO.read(is);
            BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, width, height, null);
            g.dispose();
            String ext = photo.getPath().getFileName().toString().split("\\.")[1];
            ImageIO.write(resizedImage, ext, Paths.get("images/thumbnails/" + id + "." + ext).toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
