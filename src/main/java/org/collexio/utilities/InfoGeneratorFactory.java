package org.collexio.utilities;

import org.collexio.business.domain.ItemSpec;

public class InfoGeneratorFactory {
    public static InfoGenerator getInfoGenerator (ItemSpec spec) {
        switch (spec.getType()) {
            case TECHITEM -> {
                return new GeminiInfoGenerator();
            }
            case BOOK -> {
                return new WikipediaInfoGenerator();
            }
            case PLANT -> {
                return new PFAFInfoGenerator();
            }
            default -> throw new IllegalArgumentException("Type not supported");
        }
    }
}
