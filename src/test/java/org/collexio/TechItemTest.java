package org.collexio;

import org.junit.jupiter.api.Test;

public class TechItemTest {

    @Test
    void getAvgPriceTest() {
        TechItem item = new TechItem("I-001", "nintendo ds lite rosso", 1, "Nintendo");
        System.out.println(item.getAvgPrice());
    }
}
