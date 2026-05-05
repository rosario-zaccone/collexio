package org.collexio.utilities.factory;

import io.github.cdimascio.dotenv.Dotenv;
import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.pricecraper.PriceScraper;

public abstract class AbstractFactory {
    private final String apiKey;

    protected AbstractFactory(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public abstract InfoGenerator createInfoGenerator();
    public abstract PriceScraper createPriceScraper();
}
