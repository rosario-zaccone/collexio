package org.collexio.business.service;

import org.collexio.business.domain.ItemSpec;
import org.collexio.utilities.InfoGenerator;
import org.collexio.utilities.InfoGeneratorFactory;

import java.io.IOException;

public class InfoGenerationService {
    public String generateDescriptionByAI(ItemSpec spec) throws IOException, InterruptedException {
        InfoGenerator generator = InfoGeneratorFactory.getInfoGenerator(spec);
        return generator.generateDescription(spec.getName());
    }
}
