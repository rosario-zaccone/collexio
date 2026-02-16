package org.collexio;

import org.collexio.domain.Book;
import org.collexio.domain.ItemPhoto;
import org.collexio.domain.Transaction;
import org.collexio.utilities.Utilities;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        ItemPhoto photoA = new ItemPhoto(1L, Paths.get("/home/rosario/Downloads/collexio_test/aot2.jpg"));
        //Utilities.uploadPhoto(photoA);
        itemA = new Book(1L, "Attacco dei giganti 2", 1, photoA, "attacco");

        ItemPhoto photoB = new ItemPhoto(2L, Paths.get("/home/rosario/Downloads/collexio_test/toradora.jpg"));
        //Utilities.uploadPhoto(photoB);
        itemB = new Book(2L, "Toradora 1", 1, photoB, "amore");

        ItemPhoto photoC = new ItemPhoto(3L, Paths.get("/home/rosario/Downloads/collexio_test/mha.jpg"));
        //Utilities.uploadPhoto(photoC);
        itemC = new Book(3L, "My Hero Academia 11", 2, photoC, "eroi bla bla");

        ItemPhoto photoD = new ItemPhoto(4L, Paths.get("/home/rosario/Downloads/collexio_test/note.jpg"));
        //Utilities.uploadPhoto(photoD);
        itemD = new Book(4L, "Death Note 101", 1, photoD, "robe bla bla");
    }


    @Test
    void getPriceTest() {
        assertEquals(2.7, itemA.getPrice(), 0.01);
        assertEquals(3.25, itemB.getPrice(), 0.01);
        assertEquals(2.86, itemC.getPrice(), 0.01);
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


    @Test
    void testToString() {
        System.out.println(itemA);
        System.out.println(itemB);
        System.out.println(itemC);
        System.out.println(itemD);
    }

    @Test
    void testTransactions() {
        itemA.addTransaction(new Transaction(1L, 24.5, true, LocalDate.now()));
        itemA.addTransaction(new Transaction(2L, 12.4, true, LocalDate.of(2010, 10, 10)));
        System.out.println(itemA.getTransactions());
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
