package org.collexio.utilities.factory;

import io.github.cdimascio.dotenv.Dotenv;
import org.collexio.utilities.infogenerator.GeminiInfoGenerator;
import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.pricecraper.PriceScraper;
import org.collexio.utilities.pricecraper.SubitoScraper;

public class TechItemProviderFactory extends AbstractFactory {
    public TechItemProviderFactory(Dotenv dotenv) {
        super(dotenv);
    }

    @Override
    public InfoGenerator createInfoGenerator() {
        return new GeminiInfoGenerator(getDotenv().get("GEMINI_AI_API_KEY"), 50);
    }

    @Override
    public PriceScraper createPriceScraper() {
        return new SubitoScraper();
    }
}
