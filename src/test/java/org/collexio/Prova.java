package org.collexio;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;

public class Prova {
    public static void sumOne(int[] arr) {
        for (int i = 0; i < arr.length; i++)
            arr[i] += 1;
    }

    public static int[] sumOneNs(int[] arr) {
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++)
            res[i] = arr[i] + 1;
        return res;
    }

    public static void main(String[] args) {
        int[] v = new int[2]; v[0] = 0; v[1] = 1;
        int[] r = sumOneNs(v);

        for (int e: r)
            System.out.println(e);
    }
}
