package org.collexio.utilities.infogenerator;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GeminiInfoGenerator implements InfoGenerator {
    private static final String MODEL = "gemini-2.5-flash";
    private static final String PROMPT = """
            Generate a concise description of the publication (book or comic) {{itemName}} in English. \
            Maximum {{length}} words. \
            Return plain text only — no bullet points, markdown, quotes, or special characters.
            
            Description:""";

    private final String apiKey;
    private final int descriptionMaxLength;

    public GeminiInfoGenerator(String apiKey, int descriptionMaxLength) {
        this.apiKey = apiKey;
        this.descriptionMaxLength = descriptionMaxLength;
    }


    @Override
    public String generateDescription(String itemName) {
        try (Client geminiClient = Client.builder()
                .apiKey(apiKey)
                .build()) {
            GenerateContentResponse responseGemini = geminiClient.models.generateContent(
                    MODEL,
                    PROMPT.replace("{{length}}", String.valueOf(descriptionMaxLength))
                            .replace("{{itemName}}", itemName),
                    null);

            return responseGemini.text();
        }
    }
}
