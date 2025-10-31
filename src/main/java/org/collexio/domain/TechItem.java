package org.collexio.domain;

import org.collexio.utilities.PriceScraper;
import org.collexio.utilities.SubitoScraper;
import org.collexio.utilities.WikipediaInfoGenerator;

import java.io.IOException;

public class TechItem extends Item implements Priceable {
    public TechItem(Long id, String name, int quantity, ItemPhoto photo){
        super(id, name, quantity, photo);
    }
    public TechItem(String name, int quantity, ItemPhoto photo){
        super(name, quantity, photo);
    }

    @Override
    public double getPrice() {
       PriceScraper scraper = new SubitoScraper();
       return scraper.computePrice(getName());
    }

    @Override
    public void generateDescription() {
        WikipediaInfoGenerator generator = new WikipediaInfoGenerator();
        setDescription(generator.generateDescription(getName()));
    }

    @Override
    public TechItem copy() {
        TechItem item =  new TechItem(getId(), getName(), getQuantity(), getPhoto());
        item.setDescription(getDescription());
        getTransactions().forEach(item::addTransaction);
        return item;
    }

    @Override
    public String toString() {
        return "TechItem{" + super.toString() + "}";
    }

    public String toStringNoId() {
        return "TechItem{" + super.toStringNoId() + "}";
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
