package org.collexio.utilities.pricecraper;

// PATTERN: Strategy
public interface PriceScraper {
    double computePrice(String itemName) throws InterruptedException;
}
