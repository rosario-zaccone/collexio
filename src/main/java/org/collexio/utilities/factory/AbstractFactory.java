package org.collexio.utilities.factory;

import io.github.cdimascio.dotenv.Dotenv;
import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.pricecraper.PriceScraper;

public abstract class AbstractFactory {
    private final Dotenv dotenv;

    public AbstractFactory(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    public Dotenv getDotenv() {
        return dotenv;
    }

    public abstract InfoGenerator createInfoGenerator();
    public abstract PriceScraper createPriceScraper();
}
