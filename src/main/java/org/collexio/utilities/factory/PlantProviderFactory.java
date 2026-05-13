package org.collexio.utilities.factory;

import io.github.cdimascio.dotenv.Dotenv;
import org.collexio.utilities.infogenerator.GeminiInfoGenerator;
import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.infogenerator.PFAFInfoGenerator;
import org.collexio.utilities.pricecraper.PriceScraper;

public class PlantProviderFactory extends AbstractFactory {
    public PlantProviderFactory(String apiKey) {
        super(apiKey);
    }

    @Override
    public InfoGenerator createInfoGenerator() {
        return new PFAFInfoGenerator(getApiKey(), 100);
    }

    @Override
    public PriceScraper createPriceScraper() {
        return null; // TODO
    }
}
