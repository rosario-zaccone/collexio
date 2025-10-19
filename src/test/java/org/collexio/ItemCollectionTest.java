package org.collexio;

import org.collexio.domain.Book;
import org.collexio.domain.ItemCollection;
import org.collexio.domain.ItemPhoto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ItemCollectionTest {

    private ItemCollection<Book> collection;
    private Path imagePath;

    @BeforeEach
    public void setUp() throws Exception {
        collection = new ItemCollection<>("C-001", "My Hero Academia Collection");
        imagePath = Paths.get("/home/rosario/Downloads/test.png");

        int quantity = 1;
        for (int i = 1; i <= 10; i++) {
            String id = String.format("I-%03d", i); // I-001, I-002, ...
            String name = "My Hero Academia " + i;
            if (i % 2 == 0)
                quantity = 1; // 1 2 3 4 5 6 7 8 9 10
            else
                quantity = 2;

            Book book = new Book(id, name, quantity);
            ItemPhoto photo = new ItemPhoto(imagePath, LocalDateTime.now());
            book.setPhoto(photo, 100, 100);

            collection.addItem(book);
        }
    }

    @Test
    public void testItemsAdded() {
        assertEquals(10, collection.getData().size());
    }

    @Test
    public void testTotalQuantity() {
        assertEquals(15, collection.getTotalQuantity());
    }

    @Test
    public void testBuildPhotoGeneratesPDF() throws Exception {
        collection.buildPhoto();
        File pdfFile = new File("images/collections/all_C-001");
        assertTrue(pdfFile.exists(), "PDF file should exist");
        assertTrue(pdfFile.length() > 0, "PDF file should not be empty");
        // No deletion – file is kept
    }
}
