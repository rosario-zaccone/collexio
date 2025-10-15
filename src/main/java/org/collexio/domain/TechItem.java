package org.collexio.domain;

import org.collexio.utilities.PriceScraper;
import org.collexio.utilities.SubitoScraper;
import org.collexio.utilities.WikipediaInfoGenerator;

public class TechItem extends Item implements Priceable {
    public TechItem(String id, String name, int quantity) {
        super(id, name, quantity);
    }

    @Override
    public double getPrice() {
       PriceScraper scraper = new SubitoScraper();
       return scraper.computePrice(getName());
    }

    @Override
    public String description() {
        WikipediaInfoGenerator generator = new WikipediaInfoGenerator();
        return generator.generateDescription(getName());
    }
}
