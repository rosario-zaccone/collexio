package org.collexio.utilities.factory;

import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.pricescraper.PriceScraper;

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
