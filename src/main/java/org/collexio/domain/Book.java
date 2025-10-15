package org.collexio.domain;

import org.collexio.utilities.*;

public class Book extends Item implements Priceable {
    public Book(String id, String name, int quantity) {
        super(id, name, quantity);
    }

    @Override
    public double getPrice() {
        PriceScraper scraper = new LibraccioScraper();
        return scraper.computePrice(getName());
    }

    @Override
    public String description() {
        InfoGenerator generator = new GeminiInfoGenerator();
        return generator.generateDescription(getName());
    }

}

