package org.collexio.utilities.factory;

import io.github.cdimascio.dotenv.Dotenv;
import org.collexio.AppConfig;
import org.collexio.ConfigManager;
import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.pricecraper.LibraccioScraper;
import org.collexio.utilities.pricecraper.PriceScraper;
import org.collexio.utilities.infogenerator.WikipediaInfoGenerator;

public class BookProviderFactory extends AbstractFactory {
    public BookProviderFactory(String apiKey) {
        super(apiKey);
    }

    @Override
    public InfoGenerator createInfoGenerator() {
        return new WikipediaInfoGenerator(getApiKey(), 50, (new ConfigManager()).getContactEmail());
    }

    @Override
    public PriceScraper createPriceScraper() {
        return new LibraccioScraper();
    }

}
