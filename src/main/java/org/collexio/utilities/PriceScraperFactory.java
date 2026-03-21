package org.collexio.utilities;

import org.collexio.business.domain.ItemSpec;

public class PriceScraperFactory {
    public static PriceScraper getPriceScraper (ItemSpec spec) {
        switch (spec.getType()) {
            case TECHITEM -> {
                return new SubitoScraper();
            }
            case BOOK -> {
                return new LibraccioScraper();
            }
            default -> throw new IllegalArgumentException("Type not supported");
        }
    }
}
