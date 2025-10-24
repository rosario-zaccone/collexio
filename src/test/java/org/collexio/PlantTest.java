package org.collexio;


import org.collexio.domain.ItemPhoto;
import org.collexio.domain.PhotoType;
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
        itemA = new Plant("I-1", "Primula", 1, new ItemPhoto("P-1",Paths.get("/home/rosario/Downloads/collexio_test/primula.jpg"), PhotoType.PROPIC), "primula_vulgaris");
        itemB = new Plant("I-2", "Lavender", 1, new ItemPhoto("P-2",Paths.get("/home/rosario/Downloads/collexio_test/lavander.jpg"), PhotoType.PROPIC), "lavandula_angustifolia");
        itemC = new Plant("I-3", "Rosemary", 1, new ItemPhoto("P-3",Paths.get("/home/rosario/Downloads/collexio_test/rosemary.jpg"), PhotoType.PROPIC), "rosmarinus_officinalis");
        itemD = new Plant("I-4", "Chamomile", 1, new ItemPhoto("P-4",Paths.get("/home/rosario/Downloads/collexio_test/camomilla.jpg"), PhotoType.PROPIC), "matricaria_chamomilla");
        itemE = new Plant("I-5", "Chamomile", 1, new ItemPhoto("P-5",Paths.get("/home/rosario/Downloads/collexio_test/test.png"), PhotoType.PROPIC), "wrong_name");
    }

    @Test
    void creationTest() {
        assertDoesNotThrow(() -> new Plant("I-100", "Tarassaco", 1,  new ItemPhoto("P-2", Paths.get("/home/rosario/Downloads/collexio_test/test.png"), PhotoType.PROPIC),"taraxacum_officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-2", "Tarassaco", 1, new ItemPhoto("P-2",Paths.get("/home/rosario/Downloads/collexio_test/test.png"), PhotoType.PROPIC),"taraxacumofficinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-2", "Tarassaco", 1, new ItemPhoto("P-2",Paths.get("/home/rosario/Downloads/collexio_test/test.png"), PhotoType.PROPIC),"taraxacum__officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-pippo", "Tarassaco", 1, new ItemPhoto("P-2",Paths.get("/home/rosario/Downloads/collexio_test/test.png"), PhotoType.PROPIC),"taraxacum_officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("A-pippo", "Tarassaco", 1,  new ItemPhoto("P-2",Paths.get("/home/rosario/Downloads/collexio_test/test.png"), PhotoType.PROPIC),"taraxacum_officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("A-", "Tarassaco", 1, new ItemPhoto("P-2",Paths.get("/home/rosario/Downloads/collexio_test/test.png"), PhotoType.PROPIC),"taraxacum_officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("A-001", "Tarassaco", 1, new ItemPhoto("P-2",Paths.get("/home/rosario/Downloads/collexio_test/test.png"), PhotoType.PROPIC),"taraxacum_officinale"));
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
