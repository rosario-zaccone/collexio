package org.collexio.utilities;

import java.io.IOException;

public interface InfoGenerator {
    String generateDescription(String itemName) throws IOException, InterruptedException;
}
