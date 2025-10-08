package org.collexio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TechItemTest {

    @Test
    void getAvgPriceTest() {
        TechItem item = new TechItem("I-001", "nintendo ds lite rosso", 1, "Nintendo");
        System.out.println(item.getAvgPrice());
    }

    @Test
    void descriptionTest() {
        TechItem item = new TechItem("I-001", "nintendo ds lite", 1, "Nintendo");
        System.out.println(item.description());
    }
}
