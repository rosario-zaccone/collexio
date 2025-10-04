package org.collexio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlantTest {

    @Test
    void CreationTest() {
        assertDoesNotThrow(() -> new Plant("I-002", "Tarassaco", 1, "taraxacum_officinalis"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-002", "Tarassaco", 1, "taraxacumofficinalis"));
        assertThrows(IllegalArgumentException.class, () -> new Plant("I-002", "Tarassaco", 1, "taraxacum__officinalis"));


    }
}
