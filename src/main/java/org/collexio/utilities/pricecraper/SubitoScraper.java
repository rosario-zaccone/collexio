package org.collexio.utilities.pricecraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.URI;
import java.net.http.HttpClient.Version;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubitoScraper implements PriceScraper {
    private static final String URL = "https://static-www.subito.it/annunci-italia/vendita/usato/?q=";
    private static final int MAX_PAGES = 5;
    private static final int REQUEST_DELAY_MILLIS = 2500;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);
    private static final Pattern PRICE_PATTERN = Pattern.compile(
            "(?i)(?:€\\s*|euro\\s+)(\\d{1,5})(?:[,.](\\d{1,2}))?|"
                    + "(\\d{1,5})(?:[,.](\\d{1,2}))?\\s*(?:€|euro)"
    );
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Chrome/124.0.0.0 Safari/537.36";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    public double computePrice(String itemName) throws InterruptedException {
        int value = 0;
        int count = 0;
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(REQUEST_TIMEOUT)
                .build();
        for (int page = 1; page <= MAX_PAGES; page++) {
            String responseBody = fetchPage(client, itemName, page);
            Document doc = Jsoup.parse(responseBody);
            ensureNotBlocked(doc);
            List<ScrapedItem> items = selectItems(doc);
            if (items.isEmpty()) {
                break;
            }
            for (ScrapedItem item : items) {
                if (!isRelevantTitle(item.title(), itemName)) {
                    continue;
                }
                OptionalInt price = extractPrice(item.text());
                if (price.isPresent()) {
                    value += price.getAsInt();
                    count++;
                }
            }
            Thread.sleep(REQUEST_DELAY_MILLIS);
        }
        if (count == 0) {
            throw new NoSuchElementException("Item not found");
        }
        return (double) Math.round(((double) value / count * 100)) / 100;
    }

    private String fetchPage(HttpClient client, String itemName, int page) throws InterruptedException {
        String searchUrl = URL + URLEncoder.encode(itemName.toLowerCase(), StandardCharsets.UTF_8)
                + "&qso=true&o=" + page;
        HttpRequest request = HttpRequest.newBuilder()
                .version(Version.HTTP_1_1)
                .uri(URI.create(searchUrl))
                .timeout(REQUEST_TIMEOUT)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .header("Accept-Language", "it-IT,it;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("DNT", "1")
                .header("Referer", "https://www.subito.it/")
                .header("Upgrade-Insecure-Requests", "1")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode == 403 || statusCode == 429) {
                throw new IllegalStateException("Subito blocked the request with HTTP status " + statusCode);
            }
            if (statusCode < 200 || statusCode >= 300) {
                throw new IllegalStateException("Subito returned HTTP status " + statusCode);
            }
            return response.body();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to fetch Subito search results", e);
        }
    }

    private List<ScrapedItem> selectItems(Document doc) {
        Elements articles = doc.select("article");
        if (!articles.isEmpty()) {
            List<ScrapedItem> items = new ArrayList<>();
            for (Element article : articles) {
                items.add(new ScrapedItem(article.select("h2, h3").text(), article.text()));
            }
            return items;
        }

        return selectStructuredItems(doc);
    }

    private List<ScrapedItem> selectStructuredItems(Document doc) {
        List<ScrapedItem> items = new ArrayList<>();
        for (Element script : doc.select("script[type=application/ld+json]")) {
            try {
                JsonNode graph = OBJECT_MAPPER.readTree(script.data()).path("@graph");
                if (!graph.isArray()) {
                    continue;
                }
                for (JsonNode node : graph) {
                    if (!"ImageObject".equals(node.path("@type").asText())) {
                        continue;
                    }
                    String title = firstNonBlank(node.path("name").asText(), node.path("caption").asText());
                    String text = title + " " + node.path("description").asText();
                    items.add(new ScrapedItem(title, text));
                }
            } catch (IOException ignored) {
                // Ignore non-listing JSON-LD blocks.
            }
        }
        return items;
    }

    private String firstNonBlank(String first, String second) {
        return first == null || first.isBlank() ? second : first;
    }

    private void ensureNotBlocked(Document doc) {
        String text = doc.text().toLowerCase();
        if (text.contains("access denied")
                || text.contains("captcha")
                || text.contains("robot")
                || text.contains("verifica che non sei un robot")) {
            throw new IllegalStateException("Subito anti-bot protection blocked the request");
        }
    }

    private boolean isRelevantTitle(String title, String itemName) {
        if (title == null || title.isBlank()) {
            return false;
        }
        String normalizedTitle = title.toLowerCase();
        for (String word : itemName.toLowerCase().split("[\\s\\p{Punct}]+")) {
            if (!word.isBlank() && !normalizedTitle.contains(word)) {
                return false;
            }
        }
        return true;
    }

    private OptionalInt extractPrice(String text) {
        Matcher matcher = PRICE_PATTERN.matcher(text);
        if (matcher.find()) {
            String euros = matcher.group(1) != null ? matcher.group(1) : matcher.group(3);
            return OptionalInt.of(Integer.parseInt(euros));
        }
        return OptionalInt.empty();
    }

    private record ScrapedItem(String title, String text) {}
}
