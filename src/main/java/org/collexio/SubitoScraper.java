package org.collexio;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SubitoScraper {
    private String name; // must be alphanumeric string
    private static final String url = "https://www.subito.it/annunci-italia/vendita/usato/?q=";

    private String computeUrl(String name) {
        return url + name.toLowerCase().replace(" ", "+");
    }

    public SubitoScraper(String name) {
        this.name = name; // validation TODO
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComputedUrl() {
        return computeUrl(name);
    }

    public double getAvgPrice() {
        int value;
        int count;
        try (HttpClient client = HttpClient.newHttpClient()) {
            value = 0;
            count = 0;
            int page = 1;
            while (true) {
                String searchUrl = getComputedUrl() + "&qso=true&o=" + page;
                page++;
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(searchUrl))
                        .build();
                String responseBody = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .join();  // Wait and get the response body as String
                Document doc = Jsoup.parseBodyFragment(responseBody);
                Elements items = doc.select("div.SmallCard-module_card__3hfzu.items__item.item-card.item-card--small");
                if (items.isEmpty())
                    break;
                for (Element item : items) {
                    String title = item.select("a div h2").text();
                    if (title.length() <= name.length() + 4) { // considera gli annunci il cui titolo non supera di tanto la lunghezza del nome dell'articolo
                        String price = item.select("a div div div div div div p").text();
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
        }
        // selezionare solo le cards che hanno il nome articolo nei titoli, il titolo si trova dentro il div dentro un h2 dentro uno span
        double avg = (double)value / count;
        if (count == 0)
            avg = -1; // exception ??
        return avg;
    }


}