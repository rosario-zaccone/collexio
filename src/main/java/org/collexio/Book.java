package org.collexio;

public class Book extends InanimateItem {
    public Book(String id, String name, int quantity, String creator) {
        super(id, name, quantity, creator);
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
                ", quantity=" + getQuantity() +
                ", creator='" + getCreator() + '\'' +
                '}';
    }

}

