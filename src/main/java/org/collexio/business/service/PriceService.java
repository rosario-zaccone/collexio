package org.collexio.business.service;

import org.collexio.utilities.pricecraper.PriceScraper;

// PATTERN: Strategy(context)
public class PriceService {
    private PriceScraper scraper;

    public PriceService(PriceScraper scraper) {
        this.scraper = scraper;
    }

    public void setScraper(PriceScraper scraper) {
        this.scraper = scraper;
    }

    public double computePrice(String itemName) {
        return scraper.computePrice(itemName);
    }
}
