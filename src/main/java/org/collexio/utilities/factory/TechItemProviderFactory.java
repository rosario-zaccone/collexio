package org.collexio.utilities.factory;

import org.collexio.utilities.infogenerator.GeminiInfoGenerator;
import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.pricescraper.PriceScraper;
import org.collexio.utilities.pricescraper.SubitoScraper;

public class TechItemProviderFactory extends AbstractFactory {
    public TechItemProviderFactory(String apiKey) {
        super(apiKey);
    }

    @Override
    public InfoGenerator createInfoGenerator() {
        return new GeminiInfoGenerator(getApiKey(), 50);
    }

    @Override
    public PriceScraper createPriceScraper() {
        return new SubitoScraper();
    }
}
