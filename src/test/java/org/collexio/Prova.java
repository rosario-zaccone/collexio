package org.collexio;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;

public class Prova {
    public static void main(String[] args) {
        String s = "1234ab";
        String s2 = "1234";

        var a = s2.toCharArray();
        boolean res = true;
        for (char c: a) {
            if (!Character.isDigit(c)) {
                res = false;
                break;
            }
        }

        if (res) {
            System.out.println("La stringa è un numero naturale");
        } else {
            System.out.println("La stirnga non è un numero naturale");
        }
    }
}
