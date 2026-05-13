package org.collexio.utilities.pricescraper;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

public class LibraccioScraper implements PriceScraper {
    private static final String URL = "https://www.libraccio.it/src/?FT={{itemName}}&CH=libraccio&MF=&DF=1120&UF=&PX=1&SRT=-1&MP=&EX=%24DspU%7cUsato";
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Chrome/124.0.0.0 Safari/537.36";

    public double computePrice(String itemName) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(REQUEST_TIMEOUT)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL.replace(
                        "{{itemName}}",
                        URLEncoder.encode(itemName.toLowerCase(), StandardCharsets.UTF_8)
                )))
                .timeout(REQUEST_TIMEOUT)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .header("Accept-Language", "it-IT,it;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("DNT", "1")
                .header("Referer", "https://www.libraccio.it/")
                .header("Upgrade-Insecure-Requests", "1")
                .GET()
                .build();
        String responseBody = fetchPage(client, request);
        Document doc = Jsoup.parseBodyFragment(responseBody);
        Element price = doc.select("div.item.R0.C1 div.price-outlet-container div.pru div span.sellpr").first();
        Element title = doc.select("div.item.R0.C1 div.title.title-outlet a").first();
        if (price == null || title == null) {
            throw new NoSuchElementException("Item not found");
        }
        List<String> titleWords = Arrays.asList(title.text().toLowerCase().split("[\\s\\p{Punct}]+"));
        List<String> itemWords = Arrays.asList(itemName.toLowerCase().split("[\\s\\p{Punct}]+"));
        if (!title.text().isEmpty() && new HashSet<>(titleWords).containsAll(itemWords))
            return Double.parseDouble(price.text().substring(2).replace(",", "."));
        else
            throw new NoSuchElementException("Item not found");
    }

    private String fetchPage(HttpClient client, HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode == 403 || statusCode == 429) {
                throw new IllegalStateException("Libraccio blocked the request with HTTP status " + statusCode);
            }
            if (statusCode < 200 || statusCode >= 300) {
                throw new IllegalStateException("Libraccio returned HTTP status " + statusCode);
            }
            return response.body();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to fetch Libraccio search results", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while fetching Libraccio search results", e);
        }
    }
}
