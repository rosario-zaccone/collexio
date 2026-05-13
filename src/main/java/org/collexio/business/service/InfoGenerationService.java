package org.collexio.business.service;

import org.collexio.persistence.entity.ItemType;
import org.collexio.utilities.factory.AbstractFactory;
import org.collexio.utilities.infogenerator.InfoGenerator;

import java.io.IOException;

// PATTERN: strategy (context)
public class InfoGenerationService {
    private InfoGenerator generator;
    private final AbstractFactory plantProviderFactory;
    private final AbstractFactory bookProviderFactory;
    private final AbstractFactory techItemProviderFactory;

    public InfoGenerationService(InfoGenerator generator, AbstractFactory plantProviderFactory, AbstractFactory bookProviderFactory, AbstractFactory techItemProviderFactory) {
        this.generator = generator;
        this.plantProviderFactory = plantProviderFactory;
        this.bookProviderFactory = bookProviderFactory;
        this.techItemProviderFactory = techItemProviderFactory;
    }

    public void setGenerator(ItemType itemType) {
        this.generator = switch (itemType) {
            case PLANT -> plantProviderFactory.createInfoGenerator();
            case TECHITEM -> techItemProviderFactory.createInfoGenerator();
            case BOOK -> bookProviderFactory.createInfoGenerator();
        };
    }

    public String generateDescriptionByAI(String itemName) throws IOException, InterruptedException {
        return generator.generateDescription(itemName);
    }
}
