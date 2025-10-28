package org.collexio.domain;

import org.collexio.utilities.*;

import java.io.IOException;

public class Book extends Item implements Priceable {
    public Book(int id, String name, int quantity, ItemPhoto photo) {
        super(id, name, quantity, photo);
    }

    public Book(String name, int quantity, ItemPhoto photo) {
        super(name, quantity, photo);
    }

    @Override
    public double getPrice() {
        PriceScraper scraper = new LibraccioScraper();
        return scraper.computePrice(getName());
    }

    @Override
    public void generateDescription() {
        InfoGenerator generator = new GeminiInfoGenerator();
        setDescription(generator.generateDescription(getName()));
    }

    @Override
    public Book copy()  {
        return new Book(getId(), getName(), getQuantity(), getPhoto());
    }
}

