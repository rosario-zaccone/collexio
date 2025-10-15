package org.collexio;


import org.collexio.domain.ItemPhoto;
import org.collexio.domain.Plant;
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

import static org.junit.jupiter.api.Assertions.*;

public class PlantTest {
    private static Plant itemA;
    private static Plant itemB;
    private static Plant itemC;
    private static Plant itemD;
    private static Plant itemE;

    private static final String IMAGE_PATH = "/home/rosario/Downloads/test.png";

    @BeforeAll
    public static void setUp() {
        itemA = new Plant("I-1", "Primula", 1, "primula_vulgaris");
        itemA.setPhoto(new ItemPhoto(Paths.get(IMAGE_PATH), LocalDateTime.now()), 40, 40);

        itemB = new Plant("I-2", "Lavender", 1, "lavandula_angustifolia");
        itemB.setPhoto(new ItemPhoto(Paths.get(IMAGE_PATH), LocalDateTime.now()), 40, 40);

        itemC = new Plant("I-3", "Rosemary", 1,  "rosmarinus_officinalis");
        itemC.setPhoto(new ItemPhoto(Paths.get(IMAGE_PATH), LocalDateTime.now()), 40, 40);

        itemD = new Plant("I-4", "Chamomile", 1, "matricaria_chamomilla");
        itemD.setPhoto(new ItemPhoto(Paths.get(IMAGE_PATH), LocalDateTime.now()), 40, 40);

        itemE = new Plant("I-5", "Chamomile", 1,  "wrong_name");
        itemE.setPhoto(new ItemPhoto(Paths.get(IMAGE_PATH), LocalDateTime.now()), 40, 40);
    }

    @Test
    void creationTest() {
        assertDoesNotThrow(() -> new Plant("I-2", "Tarassaco", 1,  "taraxacum_officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-2", "Tarassaco", 1,"taraxacumofficinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-2", "Tarassaco", 1, "taraxacum__officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-pippo", "Tarassaco", 1, "taraxacum_officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("A-pippo", "Tarassaco", 1,  "taraxacum_officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("A-", "Tarassaco", 1, "taraxacum_officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("A-001", "Tarassaco", 1, "taraxacum_officinale"));
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
        assertEquals("", itemE.description());
    }


    @Test
    void testGrowable() throws IOException {
        itemA.addPhoto(new ItemPhoto(Paths.get(IMAGE_PATH), LocalDateTime.now()));
        itemB.addPhoto(new ItemPhoto(Paths.get(IMAGE_PATH), LocalDateTime.now()));
        itemC.addPhoto(new ItemPhoto(Paths.get(IMAGE_PATH), LocalDateTime.now()));
        itemD.addPhoto(new ItemPhoto(Paths.get(IMAGE_PATH), LocalDateTime.now()));

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
