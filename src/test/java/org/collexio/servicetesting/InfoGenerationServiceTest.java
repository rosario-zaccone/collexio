package org.collexio.servicetesting;

import org.collexio.business.service.InfoGenerationService;
import org.collexio.persistence.entity.ItemType;
import org.collexio.utilities.factory.AbstractFactory;
import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.pricecraper.PriceScraper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InfoGenerationServiceTest {

    @Test
    void generatesDescriptionWithCurrentGenerator() throws IOException, InterruptedException {
        InfoGenerationService service =
                new InfoGenerationService(new FakeGenerator("current"), factory("plant"), factory("book"), factory("tech"));

        assertEquals("current: item", service.generateDescriptionByAI("item"));
    }

    @Test
    void setGeneratorChoosesFactoryByType() throws IOException, InterruptedException {
        InfoGenerationService service =
                new InfoGenerationService(new FakeGenerator("initial"), factory("plant"), factory("book"), factory("tech"));

        service.setGenerator(ItemType.PLANT);
        assertEquals("plant: item", service.generateDescriptionByAI("item"));

        service.setGenerator(ItemType.BOOK);
        assertEquals("book: item", service.generateDescriptionByAI("item"));

        service.setGenerator(ItemType.TECHITEM);
        assertEquals("tech: item", service.generateDescriptionByAI("item"));
    }

    private AbstractFactory factory(String prefix) {
        return new AbstractFactory("test") {
            @Override
            public InfoGenerator createInfoGenerator() {
                return new FakeGenerator(prefix);
            }

            @Override
            public PriceScraper createPriceScraper() {
                return itemName -> 0;
            }
        };
    }

    private record FakeGenerator(String prefix) implements InfoGenerator {
        @Override
        public String generateDescription(String itemName) {
            return prefix + ": " + itemName;
        }
    }
}
