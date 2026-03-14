package org.collexio.utilities;

import org.collexio.business.domain.*;

public class ItemFactory {
    public static Item getItem(int type, Long id, String name, int quantity, ItemPhoto photo, String description, String secondName) {
        return switch(type) {
            case 0 -> new Plant(id, name, quantity, photo, description, secondName);
            case 1 -> new TechItem(id, name, quantity, photo, description);
            case 2 -> new Book(id, name, quantity, photo, description);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
