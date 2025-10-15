package org.collexio;

import org.collexio.domain.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookTest {
    private static Book itemA;
    private static  Book itemB;
    private static  Book itemC;
    private static  Book itemD;

    @BeforeAll
    public static void setUp() throws IOException {
        itemA = new Book("I-1", "Attacco dei giganti 18", 1); //2.21
        itemB = new Book("I-2", "Toradora 1", 1); //2.66
        itemC = new Book("I-3", "My Hero Academia 11", 2); //2.34
        itemD = new Book("I-4", "Death Note 10", 1); // No

    }

    @Test
    void getPriceTest() {
        assertEquals(2.21, itemA.getPrice(), 0.01);
        assertEquals(2.66, itemB.getPrice(), 0.01);
        assertEquals(2.34, itemC.getPrice(), 0.01);
        assertThrows(NoSuchElementException.class, () -> {
            itemD.getPrice();
        });
    }

    @Disabled
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
