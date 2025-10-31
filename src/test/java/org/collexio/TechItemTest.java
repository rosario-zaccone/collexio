package org.collexio;

import org.collexio.domain.ItemPhoto;
import org.collexio.domain.TechItem;
import org.collexio.domain.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TechItemTest {
    private static TechItem itemA;
    private static TechItem itemB;
    private static TechItem itemC;
    private static TechItem itemD;

    @BeforeAll
    public static void setUp() throws IOException {
        String imagePath = "/home/rosario/Downloads/test.png";

        itemA = new TechItem(1L, "nintendo ds lite", 1, new ItemPhoto(20L,Paths.get("/home/rosario/Downloads/collexio_test/dslite.jpg")));
        itemB = new TechItem(2L, "pokemon heart gold", 1, new ItemPhoto(22L,Paths.get("/home/rosario/Downloads/collexio_test/pokewalker.jpg")));
        itemC = new TechItem(3L, "pokewalker", 1, new ItemPhoto(24L,Paths.get("/home/rosario/Downloads/collexio_test/pokemon.jpg")));
        itemD = new TechItem(4L, "wrong wrong wrong wrong aopaapdk", 1, new ItemPhoto(25L,Paths.get("/home/rosario/Downloads/collexio_test/test.png")));


    }

    @Disabled
    @Test
    void getPriceTest() throws Exception {
        System.out.println("price for " + itemA.getName() + ": " + itemA.getPrice());
        System.out.println("price for " + itemB.getName() + ": " +itemB.getPrice());
        System.out.println("price for " + itemC.getName() + ": " +itemC.getPrice());
        assertThrows(NoSuchElementException.class, () -> {
            itemD.getPrice();
        });

    }

    @Disabled
    @Test
    void descriptionTest() {
        itemA.generateDescription();
        System.out.println(itemA.getName() + ": " + itemA.getDescription());
        System.out.println("-----------------------------\n\n");

        itemB.generateDescription();
        System.out.println(itemB.getName() + ": " + itemB.getDescription());
        System.out.println("-----------------------------\n\n");

        itemC.generateDescription();
        System.out.println(itemC.getName() + ": " + itemC.getDescription());
        System.out.println("-----------------------------\n\n");

        itemD.generateDescription();
        System.out.println(itemD.getName() + ": " + itemD.getDescription());
        System.out.println("-----------------------------\n\n");
    }

    @Test
    void testCopy() {
        for (int i = 0; i < 4; i++)
            itemA.addTransaction(new Transaction(350 + i * 20, i % 2 == 0, LocalDateTime.now()));
        itemA.setDescription("Test");
        assertTrue(itemA.copy().toString().equals(itemA.toString()));
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
