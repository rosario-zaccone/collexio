package org.collexio;

import org.collexio.domain.Book;
import org.collexio.domain.ItemCollection;
import org.collexio.domain.ItemPhoto;
import org.collexio.domain.TechItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ItemCollectionTest {

    private ItemCollection collection;
    private Path imagePath;

    @BeforeEach
    public void setUp() throws Exception {
        collection = new ItemCollection(1L, "My Hero Academia Collection");
        Path baseImagePath = Paths.get("/home/rosario/Downloads/collexio_test/test.png");

        int quantity;
        for (long i = 1L; i <= 10; i++) {
            String name = "My Hero Academia " + i;
            quantity = (i % 2 == 0) ? 1 : 2;
            Path photoPath = Paths.get("/home/rosario/Downloads/collexio_test/test_" + i + ".png");
            ItemPhoto photo = new ItemPhoto(i, photoPath);
            Book book = new Book(i, name, quantity, photo, "manga");
            collection.addItem(book);
        }

        TechItem t = new TechItem(100L, "mha cover", 2, null, "prova");
        collection.addItem(t);
        System.out.println(collection);
    }


    @Test
    public void testItemsAdded() throws IOException {
        assertEquals(11, collection.getData().size());
    }

    @Test
    public void testTotalQuantity() {
        assertEquals(17, collection.getTotalQuantity());
    }

    @Test
    public void testBuildPhotoGeneratesPDF() throws Exception {

    }
}
