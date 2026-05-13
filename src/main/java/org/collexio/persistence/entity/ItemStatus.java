package org.collexio.persistence.entity;

public enum ItemStatus {
    BAD(0),
    AVERAGE(1),
    GOOD(2);

    private final int value;
    ItemStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ItemStatus fromInt(int value) {
        return switch (value) {
            case 0 -> BAD;
            case 1 -> AVERAGE;
            case 2 -> GOOD;
            default -> throw new IllegalArgumentException("Invalid value");
        };
    }

    @Override
    public String toString() {
        return switch (value) {
            case 0 -> "Bad";
            case 1 -> "Average";
            case 2 -> "Good";
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }
}
