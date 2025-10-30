package org.collexio.utilities;

import org.collexio.domain.*;

public class ItemFactory {
    public static Item getItem(int type, int id, String name, int quantity, ItemPhoto photo, String secondName) {
        return switch(type) {
            case 0 -> new Plant(id, name, quantity, photo, secondName);
            case 1 -> new TechItem(id, name, quantity, photo);
            case 2 -> new Book(id, name, quantity, photo);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
