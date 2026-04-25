package org.collexio.utilities.infogenerator;

import java.io.IOException;

// PATTERN: strategy
public interface InfoGenerator {
    String generateDescription(String itemName) throws IOException, InterruptedException;
}
