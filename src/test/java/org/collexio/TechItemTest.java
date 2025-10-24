package org.collexio;

import org.collexio.domain.ItemPhoto;
import org.collexio.domain.PhotoType;
import org.collexio.domain.TechItem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class TechItemTest {
    private static TechItem itemA;
    private static TechItem itemB;
    private static TechItem itemC;
    private static TechItem itemD;

    @BeforeAll
    public static void setUp() throws IOException {
        String imagePath = "/home/rosario/Downloads/test.png";

        itemA = new TechItem("I-1", "nintendo ds lite", 1, new ItemPhoto("P-20",Paths.get("/home/rosario/Downloads/collexio_test/dslite.jpg"), PhotoType.PROPIC));
        itemB = new TechItem("I-2", "pokemon heart gold", 1, new ItemPhoto("P-22",Paths.get("/home/rosario/Downloads/collexio_test/pokewalker.jpg"), PhotoType.PROPIC));
        itemC = new TechItem("I-3", "pokewalker", 1, new ItemPhoto("P-24",Paths.get("/home/rosario/Downloads/collexio_test/pokemon.jpg"), PhotoType.PROPIC));
        itemD = new TechItem("I-4", "wrong wrong wrong wrong aopaapdk", 1, new ItemPhoto("P-25",Paths.get("/home/rosario/Downloads/collexio_test/test.png"), PhotoType.PROPIC));


    }

    @Test
    void getPriceTest() throws Exception {
        System.out.println("price for " + itemA.getName() + ": " + itemA.getPrice());
        System.out.println("price for " + itemB.getName() + ": " +itemB.getPrice());
        System.out.println("price for " + itemC.getName() + ": " +itemC.getPrice());
        assertThrows(NoSuchElementException.class, () -> {
            itemD.getPrice();
        });

    }

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
