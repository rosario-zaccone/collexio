package org.collexio.utilities.infogenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WikipediaInfoGenerator implements InfoGenerator {
    private static final String MODEL = "gemini-2.5-flash";
    private static final String WIKIPEDIA_SEARCH_URL =  "https://en.wikipedia.org/w/rest.php/v1/search/page?q={{itemName}}&limit=1";
    private static final String WIKIPEDIA_EXTRACT_URL = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&pageids={{itemId}}&exintro=true&explaintext=true";
    private static final String PROMPT = """
            Generate a concise description of the following book in English. \
            Maximum {{length}} words. \
            Return plain text only — no bullet points, markdown, quotes, or special characters.
            
            Item data:
             {{itemData}}
            
            Description:""";
    private final String apiKey;
    private final int descriptionMaxLength;
    private final String contactMail;

    public WikipediaInfoGenerator(String apiKey, int descriptionMaxLength, String contactMail) {
        this.apiKey = apiKey;
        this.descriptionMaxLength = descriptionMaxLength;
        this.contactMail = contactMail;
    }

    @Override
    public String generateDescription(String itemName) throws IOException, InterruptedException {
        String id;
        try (Client geminiClient = Client.builder()
                .apiKey(apiKey)
                .build()) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(WIKIPEDIA_SEARCH_URL.replace("{{itemName}}", itemName.replace(" ", "%20"))))
                    .header("User-Agent", "MyWikipediaBot/1.0 (contact: " + contactMail + ")")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                throw new RuntimeException("Failed to get page: HTTP status " + statusCode);
            }
            String responseBody = response.body();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode pages = root.path("pages");
            if (pages.isArray() && !pages.isEmpty()) {
                JsonNode firstPage = pages.get(0);
                id = String.valueOf(firstPage.path("id"));
            } else {
                throw new RuntimeException("No pages found in response");
            }

            request = HttpRequest.newBuilder()
                    .uri(URI.create(WIKIPEDIA_EXTRACT_URL.replace("{{itemId}}", id )))
                    .header("User-Agent", "MyWikipediaBot/1.0 (contact: " + contactMail + ")")
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            statusCode = response.statusCode();
            if (statusCode != 200) {
                throw new RuntimeException("Failed to get page: HTTP status " + statusCode);
            }
            responseBody = response.body();
            root = mapper.readTree(responseBody);
            JsonNode node = root.path("query").path("pages").path(id);
            String itemData = node.path("extract").toString();
            GenerateContentResponse responseGemini = geminiClient.models.generateContent(
                    MODEL,
                    PROMPT.replace("{{length}}", String.valueOf(descriptionMaxLength))
                            .replace("{{itemData}}", itemData),
                    null);
            return responseGemini.text();

        }
    }
}
