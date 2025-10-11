package org.collexio;

import org.collexio.domain.TechItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class TechItemTest {
    private static TechItem itemA;
    private static TechItem itemB;
    private static TechItem itemC;
    private static TechItem itemD;

    @BeforeAll
    public static void setUp() {
        itemA = new TechItem("I-1", "nintendo ds lite", 1);
        itemB = new TechItem("I-2", "pokemon heart gold", 1);
        itemC = new TechItem("I-3", "pokewalker", 1);
        itemD = new TechItem("I-4", "wrong wrong wrong wrong aopaapdk", 1);
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
