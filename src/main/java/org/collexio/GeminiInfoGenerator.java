package org.collexio;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GeminiInfoGenerator implements InfoGenerator {
    @Override
    public String generateDescription(String itemName) {
        try (Client geminiClient = new Client()) {
            String prompt = "Generate a concise description of the item " + itemName +", maximum 250 words." +
                    "Provide the output as plain text without any special formatting or markup. " +
                    "This content will be used as a Java String.";

            GenerateContentResponse responseGemini = geminiClient.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null);

            return responseGemini.text();
        }
    }
}
