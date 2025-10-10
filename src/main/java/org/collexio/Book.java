package org.collexio;

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

    @Override
    public String toString() {
        return "Book{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", quantity=" + getQuantity() + '\'' +
                '}';
    }

}

