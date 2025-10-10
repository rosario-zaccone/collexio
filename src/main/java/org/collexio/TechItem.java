package org.collexio;

public class TechItem extends Item implements Priceable{
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

    @Override
    public String toString() {
        return "TechItem{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", quantity=" + getQuantity() + '\'' +
                '}';
    }

}
