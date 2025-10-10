package org.collexio;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlantTest {
    private static Plant itemA;
    private static Plant itemB;
    private static Plant itemC;
    private static Plant itemD;

    @BeforeAll
    public static void setUp() {
        itemA = new Plant("I-001", "Primula", 1, "primula_vulgaris");
        itemB = new Plant("I-002", "Lavender", 1, "lavandula_angustifolia");
        itemC = new Plant("I-003", "Rosemary", 1, "rosmarinus_officinalis");
        itemD = new Plant("I-004", "Chamomile", 1, "matricaria_chamomilla");
    }

    @Test
    void creationTest() {
        assertDoesNotThrow(() -> new Plant("I-002", "Tarassaco", 1, "taraxacum_officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-002", "Tarassaco", 1, "taraxacumofficinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-002", "Tarassaco", 1, "taraxacum__officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-pippo", "Tarassaco", 1, "taraxacum_officinale"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("A-pippo", "Tarassaco", 1, "taraxacum_officinale"));
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
        Plant p1 = new Plant("I-001", "Primula", 1, "primula_vulgaris");
        Plant p2 = new Plant("I-002", "Primula", 1, "wrong_name");
        assertEquals("", p2.description());
        assertFalse(p1.description().isEmpty());
    }
}
