package org.collexio.servicetesting;

import org.collexio.business.service.PriceService;
import org.collexio.persistence.entity.ItemType;
import org.collexio.utilities.factory.AbstractFactory;
import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.pricecraper.PriceScraper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceServiceTest {

    @Test
    void computesPriceWithCurrentScraper() throws InterruptedException {
        PriceService service = new PriceService(new FakeScraper(12.5), factory(1), factory(2), factory(3));

        assertEquals(12.5, service.computePrice("book"));
    }

    @Test
    void setScraperChoosesFactoryByType() throws InterruptedException {
        PriceService service = new PriceService(new FakeScraper(0), factory(1), factory(2), factory(3));

        service.setScraper(ItemType.PLANT);
        assertEquals(1, service.computePrice("plant"));

        service.setScraper(ItemType.BOOK);
        assertEquals(2, service.computePrice("book"));

        service.setScraper(ItemType.TECHITEM);
        assertEquals(3, service.computePrice("tech"));
    }

    private AbstractFactory factory(double value) {
        return new AbstractFactory("test") {
            @Override
            public InfoGenerator createInfoGenerator() {
                return itemName -> "unused";
            }

            @Override
            public PriceScraper createPriceScraper() {
                return new FakeScraper(value);
            }
        };
    }

    private record FakeScraper(double value) implements PriceScraper {
        @Override
        public double computePrice(String itemName) {
            return value;
        }
    }
}
