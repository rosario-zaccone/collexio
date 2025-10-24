package org.collexio.utilities;
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
    private static final String url = "https://www.subito.it/annunci-italia/vendita/usato/?q=";


    public double computePrice(String itemName) {
        int value;
        int count;
        HttpClient client = HttpClient.newHttpClient();
        value = 0;
        count = 0;
        int page = 1;
        while (true) {
            String searchUrl = url + itemName.toLowerCase().replace(" ", "+") + "&qso=true&o=" + page;
            page++;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(searchUrl))
                    .build();
            String responseBody = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();  // Wait and get the response body as String
            Document doc = Jsoup.parseBodyFragment(responseBody);
            Elements items = doc.select("div article"); // to improve
            if (items.isEmpty())
                break;
            for (Element item : items) {
                String title = item.select("section.AdItemCardResponsive_details__ZxI0t h3").text();
                if (title.length() <= itemName.length() + 4) { // considera gli annunci il cui titolo non supera di tanto la lunghezza del nome dell'articolo
                    String price = item.select("section div section  div p").text();
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
        }
        if (count == 0)
            throw new NoSuchElementException("Item not found");
        // selezionare solo le cards che hanno il nome articolo nei titoli, il titolo si trova dentro il div dentro un h2 dentro uno span
        return (double) Math.round(((double) value / count * 100)) /100;
    }


}