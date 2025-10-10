package org.collexio;

import org.collexio.domain.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BookTest {
    private static Book itemA;
    private static  Book itemB;
    private static  Book itemC;
    private static  Book itemD;

    @BeforeAll
    public static void setUp() {
        itemA = new Book("I-001", "Attack on titan 1", 1);
        itemB = new  Book("I-002", "Toradora 1", 1);
        itemC = new  Book("I-003", "My Hero Academia 11", 1);
        itemD = new  Book("I-004", "Death Note 10", 1);
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
}
