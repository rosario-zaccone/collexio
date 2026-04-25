package org.collexio.business.service;

import org.collexio.utilities.infogenerator.InfoGenerator;

import java.io.IOException;

// PATTERN: strategy (context)
public class InfoGenerationService {
    private InfoGenerator generator;

    public InfoGenerationService(InfoGenerator generator) {
        this.generator = generator;
    }

    public void setGenerator(InfoGenerator generator) {
        this.generator = generator;
    }

    public String generateDescriptionByAI(String itemName) throws IOException, InterruptedException {
        return generator.generateDescription(itemName);
    }
}
