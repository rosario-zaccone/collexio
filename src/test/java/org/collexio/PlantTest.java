package org.collexio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlantTest {

    @Test
    void CreationTest() {
        assertDoesNotThrow(() -> new Plant("I-002", "Tarassaco", 1, "taraxacum_officinalis"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-002", "Tarassaco", 1, "taraxacumofficinalis"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-002", "Tarassaco", 1, "taraxacum__officinalis"));
    }

    @Test
    void descriptionTest() {
        Plant p1 = new Plant("I-001", "Primula", 1, "primula_vulgaris");
        Plant p2 = new Plant("I-002", "Primula", 1, "wrong_name");
        assertEquals("", p2.description());
        assertFalse(p1.description().isEmpty());
    }
}
