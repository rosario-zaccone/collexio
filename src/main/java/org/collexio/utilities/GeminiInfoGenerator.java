package org.collexio.utilities;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GeminiInfoGenerator implements InfoGenerator {
    private final String model;

    public GeminiInfoGenerator (String model) {
        this.model = model;
    }

    public GeminiInfoGenerator () {
        this("gemini-2.5-flash");
    }

    @Override
    public String generateDescription(String itemName) {
        try (Client geminiClient = new Client()) {
            String prompt = "Generate a concise description of the book/comic/manga " + itemName +", maximum 150 words." +
                    "Provide the output as plain text without any special formatting or markup. " +
                    "This content will be used as a Java String for a description of a item in an inventory." +
                    "For example: DragonBall is a manga written by Toriyama and illustrate the adventure of Goku, the protegonist...";

            GenerateContentResponse responseGemini = geminiClient.models.generateContent(
                    model,
                    prompt,
                    null);

            return responseGemini.text();
        }
    }
}
