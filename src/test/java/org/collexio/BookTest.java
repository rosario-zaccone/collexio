package org.collexio;

import org.junit.jupiter.api.Test;

public class BookTest {

    @Test
    void getAvgPriceTest() {
        InanimateItem item = new Book("I-001", "Attack on Titan 2", 1, "Isayama");
        System.out.println(item.getAvgPrice());
    }

    @Test
    void descriptionTest() {
        InanimateItem item = new Book("I-001", "Attack on Titan 2", 1, "Isayama");
        System.out.println(item.description());
    }
}