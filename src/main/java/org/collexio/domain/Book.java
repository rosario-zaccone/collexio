package org.collexio.domain;

import org.collexio.utilities.GeminiInfoGenerator;
import org.collexio.utilities.InfoGenerator;
import org.collexio.utilities.SubitoScraper;

import java.io.IOException;

public class Book extends Item implements Priceable {
    public Book(String id, String name, int quantity) {
        super(id, name, quantity);
    }

    @Override
    public double getAvgPrice() {
        SubitoScraper scraper = new SubitoScraper();
        return scraper.getAvgPrice(getName());
    }

    @Override
    public String description() {
        InfoGenerator generator = new GeminiInfoGenerator();
        return generator.generateDescription(getName());
    }

}

