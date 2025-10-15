package org.collexio.utilities;

import java.util.NoSuchElementException;

public interface PriceScraper {
    double computePrice(String itemName);
}
