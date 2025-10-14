package org.collexio.domain;

import org.collexio.utilities.SubitoScraper;
import org.collexio.utilities.WikipediaInfoGenerator;

import java.io.IOException;

public class TechItem extends Item implements Priceable {
    public TechItem(String id, String name, int quantity) {
        super(id, name, quantity);
    }

    @Override
    public double getAvgPrice() {
       SubitoScraper scraper = new SubitoScraper();
       return scraper.getAvgPrice(getName());
    }

    @Override
    public String description() {
        WikipediaInfoGenerator generator = new WikipediaInfoGenerator();
        return generator.generateDescription(getName());
    }
}
