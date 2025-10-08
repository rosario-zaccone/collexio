package org.collexio;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;

public class Prova {
    public static void main(String[] args) {
        InfoGenerator g = new WikipediaInfoGenerator();
        System.out.println(g.generateDescription("nintendo ds"));
    }
}
