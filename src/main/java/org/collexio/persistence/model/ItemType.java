package org.collexio.persistence.model;

public enum ItemType {
    PLANT(0),
    TECHITEM(1),
    BOOK(2);

    private final int value;
    ItemType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ItemType fromInt(int value) {
        switch (value) {
            case 0: return PLANT;
            case 1: return TECHITEM;
            case 2: return BOOK;
            default: throw new IllegalArgumentException("Invalid value");
        }
    }
}

// aggiungere tipo item a classi e diagrammi uml, elimianr ecolonna scientific name da database