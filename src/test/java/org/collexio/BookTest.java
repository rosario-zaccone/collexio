package org.collexio;

import org.collexio.domain.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BookTest {
    private static Book itemA;
    private static  Book itemB;
    private static  Book itemC;
    private static  Book itemD;

    @BeforeAll
    public static void setUp() throws IOException {
        itemA = new Book("I-1", "Attack on titan 1", 1);
        itemB = new Book("I-2", "Toradora 1", 1);
        itemC = new Book("I-3", "My Hero Academia 11", 2);
        itemD = new Book("I-4", "Death Note 10", 1);

    }

    @Test
    void getAvgPriceTest() {
        System.out.println(itemA.getAvgPrice());
        System.out.println(itemB.getAvgPrice());
        System.out.println(itemC.getAvgPrice());
        System.out.println(itemD.getAvgPrice());

    }

    @Test
    void descriptionTest() {
        System.out.println(itemA.getName() + ": " + itemA.description());
        System.out.println("-----------------------------\n\n");
        System.out.println(itemB.getName() + ": " + itemB.description());
        System.out.println("-----------------------------\n\n");
        System.out.println(itemC.getName() + ": " + itemC.description());
        System.out.println("-----------------------------\n\n");
        System.out.println(itemD.getName() + ": " + itemD.description());
        System.out.println("-----------------------------\n\n");
    }

    @Test
    void testToString() {
        System.out.println(itemA);
        System.out.println(itemB);
        System.out.println(itemC);
        System.out.println(itemD);
    }

    @AfterAll
    static void deleteImages() {
        Path folder = Paths.get("images/thumbnails");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
            for (Path path : stream) {
                Files.delete(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
