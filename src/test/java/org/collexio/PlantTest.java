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
    public static void setUp() throws IOException {
        itemA = new Plant(1L, "Primula", 1, new ItemPhoto(1L,Paths.get("/home/rosario/Downloads/collexio_test/primula.jpg")), "primula_vulgaris");
        itemB = new Plant(2L, "Lavender", 1, new ItemPhoto(2L,Paths.get("/home/rosario/Downloads/collexio_test/lavander.jpg")), "lavandula_angustifolia");
        itemC = new Plant(3L, "Rosemary", 1, new ItemPhoto(3L,Paths.get("/home/rosario/Downloads/collexio_test/rosemary.jpg")), "rosmarinus_officinalis");
        itemD = new Plant(4L, "Chamomile", 1, new ItemPhoto(4L,Paths.get("/home/rosario/Downloads/collexio_test/camomilla.jpg")), "matricaria_chamomilla");
        itemE = new Plant(5L, "Chamomile", 1, new ItemPhoto(5L,Paths.get("/home/rosario/Downloads/collexio_test/test.png")), "wrong_name");
    }

    @Test
    void creationTest() {
        assertDoesNotThrow(() -> new Plant(100L, "Tarassaco", 1,  new ItemPhoto(2L, Paths.get("/home/rosario/Downloads/collexio_test/test.png")),"taraxacum_officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant(-2L, "Tarassaco", 1, new ItemPhoto(2L,Paths.get("/home/rosario/Downloads/collexio_test/test.png")),"taraxacumofficinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant(2L, "Tarassaco", 1, new ItemPhoto(2L,Paths.get("/home/rosario/Downloads/collexio_test/test.png")),"taraxacum__officinale"));
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
