package org.collexio.utilities;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Optional;

public class PFAFInfoGenerator implements InfoGenerator {
    private static final String baseUrl = "https://pfaf.org/user/Plant.aspx?LatinName=";

    @Override
    public String generateDescription(String itemName) throws IOException, InterruptedException {
        Dotenv dotenv = Dotenv.load();
        try (Client geminiClient = Client.builder()
                .apiKey(dotenv.get("GEMINI_AI_API_KEY"))
                .build()) {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + itemName.replace("_", "+")))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                throw new RuntimeException("Failed to get page: HTTP status " + statusCode);
            }
            String responseBody = response.body();

            Document doc = Jsoup.parseBodyFragment(responseBody);

            String tableContent = Optional.ofNullable(doc.selectFirst("table.table-hover.table-striped"))
                    .map(Element::text)
                    .orElse("");

            String phyContent = Optional.ofNullable(doc.selectFirst("span#ContentPlaceHolder1_lblPhystatment"))
                    .map(Element::text)
                    .orElse("");

            String summaryContent = Optional.ofNullable(doc.selectFirst("span#ContentPlaceHolder1_txtSummary"))
                    .map(Element::text)
                    .orElse("");
            if (tableContent.isEmpty() || phyContent.isEmpty() || summaryContent.isEmpty())
                throw new IllegalArgumentException("You must use a valid plant name");

            String prompt = "Generate a concise description of the plant, maximum 100 words, based on the following data: " +
                    "General information: " + tableContent + "; " + summaryContent + ". " +
                    "Physical characteristics: " + phyContent + ". " +
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
