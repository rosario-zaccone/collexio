package org.collexio.utilities.pricecraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.NoSuchElementException;

public class SubitoScraper implements PriceScraper {
    private static final String URL = "https://www.subito.it/annunci-italia/vendita/usato/?q=";


    public double computePrice(String itemName) throws InterruptedException {
        int value = 0;
        int count = 0;
        int page = 1;
        HttpClient client = HttpClient.newHttpClient();
        while (true) {
            String searchUrl = URL + itemName.toLowerCase().replace(" ", "+") + "&qso=true&o=" + page;
            page++;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(searchUrl))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .header("Accept-Language", "it-IT,it;q=0.9")
                    .GET()
                    .build();
            String responseBody = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();
            Document doc = Jsoup.parseBodyFragment(responseBody);
            System.out.println(doc);
            Elements items = doc.select("div article");
            if (items.isEmpty())
                break;
            for (Element item : items) {
                String title = item.select("section h3").text();
                System.out.println("[SCRAPING] TITLE: " + title);
                if (title.length() <= itemName.length() + 4) {
                    String price = item.select("section div p").text();
                    StringBuilder nPrice = new StringBuilder();
                    // price extraction
                    for (int i = 0; i < price.length(); i++) {
                        if (Character.isDigit(price.charAt(i)))
                            nPrice.append(price.charAt(i));
                    }

                    if (!nPrice.toString().isEmpty()) {
                        value += Integer.parseInt(nPrice.toString());
                        count++;
                    }
                }
            }
            Thread.sleep(2000);
        }
        if (count == 0)
            throw new NoSuchElementException("Item not found");
        return (double) Math.round(((double) value / count * 100)) /100;
    }


}