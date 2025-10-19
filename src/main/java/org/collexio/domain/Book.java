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
    public void generateDescription() {
        InfoGenerator generator = new GeminiInfoGenerator();
        setDescription(generator.generateDescription(getName()));
    }

}

