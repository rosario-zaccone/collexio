package org.collexio.utilities.factory;

import io.github.cdimascio.dotenv.Dotenv;
import org.collexio.utilities.infogenerator.GeminiInfoGenerator;
import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.pricecraper.PriceScraper;
import org.collexio.utilities.pricecraper.SubitoScraper;

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
