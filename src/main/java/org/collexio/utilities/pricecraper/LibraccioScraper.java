package org.collexio.utilities.pricecraper;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

public class LibraccioScraper implements PriceScraper {
    private static final String URL = "https://www.libraccio.it/src/?FT={{itemName}}&CH=libraccio&MF=&DF=1120&UF=&PX=1&SRT=-1&MP=&EX=%24DspU%7cUsato";

    public double computePrice(String itemName) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL.replace("{{itemName}}", itemName.replace(" ", "+").toLowerCase())))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "it-IT,it;q=0.9,en;q=0.8")
                .GET()
                .build();
        String responseBody = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();  // Wait and get the response body as String
        Document doc = Jsoup.parseBodyFragment(responseBody);
        Element price = doc.select("div.item.R0.C1 div.price-outlet-container div.pru div span.sellpr").first();
        Element title = doc.select("div.item.R0.C1 div.title.title-outlet a").first();
        List<String> titleWords = Arrays.asList(title.text().toLowerCase().split("[\\s\\p{Punct}]+"));
        List<String> itemWords = Arrays.asList(itemName.toLowerCase().split("[\\s\\p{Punct}]+"));
        if (price != null && !title.text().isEmpty() && new HashSet<>(titleWords).containsAll(itemWords))
            return Double.parseDouble(price.text().substring(2).replace(",", "."));
        else
            throw new NoSuchElementException("Item not found");
    }

}
