package org.collexio.utilities;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class LibraccioScraper implements PriceScraper {
    private static final String url = "https://www.libraccio.it/src/?FT={ITEM_NAME}&CH=libraccio&MF=&DF=1120&UF=&PX=1&SRT=-1&MP=&EX=%24DspU%7cUsato";

    public double computePrice(String itemName) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url.replace("{ITEM_NAME}", itemName.replace(" ", "+").toLowerCase())))
                .build();
        String responseBody = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();  // Wait and get the response body as String
        Document doc = Jsoup.parseBodyFragment(responseBody);
        Element price = doc.select("div.item.R0.C1 div.price-outlet-container div.pru div span.sellpr").first();
        Element title = doc.select("div.item.R0.C1 div.title.title-outlet a").first();
        List<String> titleWords = Arrays.asList(title.text().toLowerCase().split("[\\s\\p{Punct}]+"));
        List<String> itemWords = Arrays.asList(itemName.toLowerCase().split("[\\s\\p{Punct}]+"));
        if (price != null && !title.text().isEmpty() && itemWords.stream()
                .allMatch(titleWords::contains))
            return Double.parseDouble(price.text().substring(2).replace(",", "."));
        else
            throw new NoSuchElementException("Item not found");
    }

}
