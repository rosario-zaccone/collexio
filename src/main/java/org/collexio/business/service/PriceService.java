package org.collexio.business.service;

import org.collexio.business.domain.ItemSpec;
import org.collexio.utilities.PriceScraper;
import org.collexio.utilities.PriceScraperFactory;

public class PriceService {
    public double computePrice(ItemSpec spec) {
        PriceScraper scraper = PriceScraperFactory.getPriceScraper(spec);
        return scraper.computePrice(spec.getName());
    }
}
