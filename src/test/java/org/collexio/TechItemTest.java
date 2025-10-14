package org.collexio;

import org.collexio.domain.ItemPhoto;
import org.collexio.domain.TechItem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;


public class TechItemTest {
    private static TechItem itemA;
    private static TechItem itemB;
    private static TechItem itemC;
    private static TechItem itemD;

    @BeforeAll
    public static void setUp() throws IOException {
        String imagePath = "/home/rosario/Downloads/test.png";

        itemA = new TechItem("I-1", "nintendo ds lite", 1);
        itemB = new TechItem("I-2", "pokemon heart gold", 1);
        itemC = new TechItem("I-3", "pokewalker", 1);
        itemD = new TechItem("I-4", "wrong wrong wrong wrong aopaapdk", 1);


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
