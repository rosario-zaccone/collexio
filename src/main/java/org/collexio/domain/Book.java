package org.collexio.domain;

import org.collexio.utilities.*;

import java.io.IOException;

public class Book extends Item implements Priceable {
    public Book(Long id, String name, int quantity, ItemPhoto photo, String description) {
        super(id, name, quantity, photo, description);
    }

    public Book(Long id, String name, int quantity, ItemPhoto photo) {
        this(id, name, quantity, photo, "no description");
    }

    public Book(String name, int quantity, ItemPhoto photo, String description) {
        this(null, name, quantity, photo, description);
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
        Book item =  new Book(getId(), getName(), getQuantity(), getPhoto(), getDescription());
        item.getTransactions().forEach(item::addTransaction);
        return item;
    }

    @Override
    public String toString() {
        return "Book{" + super.toString() + "}";
    }

    public String toStringNoId() {
        return "Book{" + super.toStringNoId() + "}";
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}

