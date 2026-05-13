package org.collexio.utilities.pricescraper;

// PATTERN: Strategy
public interface PriceScraper {
    double computePrice(String itemName) throws InterruptedException;
}
