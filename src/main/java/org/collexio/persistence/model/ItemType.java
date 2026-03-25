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
        return switch (value) {
            case 0 -> PLANT;
            case 1 -> TECHITEM;
            case 2 -> BOOK;
            default -> throw new IllegalArgumentException("Invalid value");
        };
    }

    @Override
    public String toString() {
        return switch (value) {
            case 0 -> "Plant";
            case 1 -> "Tech Item";
            case 2 -> "Book";
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    public static ItemType fromString(String value) {
        return switch (value) {
            case "Plant" -> PLANT;
            case "Tech Item" -> TECHITEM;
            case "Book" -> BOOK;
            default -> throw new IllegalArgumentException("Invalid value");
        };
    }
}
