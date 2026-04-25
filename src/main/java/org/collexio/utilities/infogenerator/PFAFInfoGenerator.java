package org.collexio.utilities.infogenerator;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class PFAFInfoGenerator implements InfoGenerator {
    private static final String MODEL = "gemini-2.5-flash";
    private static final String URL = "https://pfaf.org/user/Plant.aspx?LatinName=";
    private static final String PROMPT = """
            Generate a concise description of the plant in English. \
            Maximum {{length}} words. \
            Return plain text only — no bullet points, markdown, quotes, or special characters.
            
            General information:
            {{tableContent}}
            {{summaryContent}}
            
            Physical characteristics:
            {{phyContent}}
            
            Description:""";
    private final String apiKey;
    private final int descriptionMaxLength;

    public PFAFInfoGenerator(String apiKey, int descriptionMaxLength) {
        this.apiKey = apiKey;
        this.descriptionMaxLength = descriptionMaxLength;
    }

    @Override
    public String generateDescription(String itemName) throws IOException, InterruptedException {
        try (Client geminiClient = Client.builder()
                .apiKey(apiKey)
                .build()) {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + itemName.replace("_", "+")))
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

            GenerateContentResponse responseGemini = geminiClient.models.generateContent(
                    MODEL,
                    PROMPT.replace("{{length}}", String.valueOf(descriptionMaxLength))
                            .replace("{{tableContent}}", tableContent)
                            .replace("{{summaryContent}}", summaryContent)
                            .replace("{{phyContent}}", phyContent),
                    null);

            return responseGemini.text();

        }
    }

}
