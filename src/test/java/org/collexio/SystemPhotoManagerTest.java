package org.collexio;

import org.collexio.domain.Item;
import org.collexio.domain.ItemPhoto;
import org.collexio.domain.Plant;
import org.collexio.utilities.SystemPhotoManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class SystemPhotoManagerTest {
    private static Plant itemA;
    private static final String IMAGE_PATH = "/home/rosario/Downloads/test.png";

    @BeforeAll
    public static void setUp() throws IOException {
        itemA = new Plant("I-1", "Primula", 1, "primula_vulgaris");
    }

    @Test
    public void addPhoto() throws IOException {
        SystemPhotoManager manager = new SystemPhotoManager("./images/growable/");
        manager.addPhoto(itemA.getId(), new ItemPhoto(Paths.get(IMAGE_PATH), LocalDateTime.now()));
    }

    @Test
    public void getPhotos() throws IOException {
        SystemPhotoManager manager = new SystemPhotoManager("./images/growable/");
        manager.getPhotos(itemA.getId()).forEach(System.out::println);
    }
}
