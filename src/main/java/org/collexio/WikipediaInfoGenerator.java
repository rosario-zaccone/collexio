package org.collexio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WikipediaInfoGenerator implements InfoGenerator {
    private static final String baseUrl =  "https://en.wikipedia.org/w/rest.php/v1/search/page?q={ITEM_NAME}&limit=1";
    private static final String infoUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&pageids={ITEM_ID}&exintro=true&explaintext=true";

    @Override
    public String generateDescription(String itemName) {
        String id;
        try (Client geminiClient = new Client()) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl.replace("{ITEM_NAME}", itemName.replace(" ", "%20"))))
                    .header("User-Agent", "MyWikipediaBot/1.0 (contact: rosariozaccone999@gmail.com)")
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
                    .uri(URI.create(infoUrl.replace("{ITEM_ID}", id )))
                    .header("User-Agent", "MyWikipediaBot/1.0 (contact: rosariozaccone999@gmail.com)")
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            statusCode = response.statusCode();
            if (statusCode != 200) {
                throw new RuntimeException("Failed to get page: HTTP status " + statusCode);
            }
            responseBody = response.body();
            root = mapper.readTree(responseBody);
            JsonNode node = root.path("query").path("pages").path(id);
            String prompt = "Generate a concise description of the item, maximum 250 words, based on the following data: " +
                    node.path("extract").toString() +
                    "Provide the output as plain text without any special formatting or markup. " +
                    "This content will be used as a Java String.";
            GenerateContentResponse responseGemini = geminiClient.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null);
            return responseGemini.text();

        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            return "";
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }
}
