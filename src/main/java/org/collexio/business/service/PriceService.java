package org.collexio.business.service;

import org.collexio.persistence.entity.ItemType;
import org.collexio.utilities.factory.AbstractFactory;
import org.collexio.utilities.pricecraper.PriceScraper;

// PATTERN: Strategy(context)
public class PriceService {
    private PriceScraper scraper;
    private final AbstractFactory plantProviderFactory;
    private final AbstractFactory bookProviderFactory;
    private final AbstractFactory techItemProviderFactory;

    public PriceService(PriceScraper scraper, AbstractFactory plantProviderFactory, AbstractFactory bookProviderFactory, AbstractFactory techItemProviderFactory) {
        this.scraper = scraper;
        this.plantProviderFactory = plantProviderFactory;
        this.bookProviderFactory = bookProviderFactory;
        this.techItemProviderFactory = techItemProviderFactory;
    }

    public void setScraper(ItemType itemType) {
        this.scraper = switch (itemType) {
            case PLANT -> plantProviderFactory.createPriceScraper();
            case TECHITEM -> techItemProviderFactory.createPriceScraper();
            case BOOK -> bookProviderFactory.createPriceScraper();
        };
    }

    public double computePrice(String itemName) throws InterruptedException {
        return scraper.computePrice(itemName);
    }
}
