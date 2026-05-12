package org.collexio.business.domain;

public class InvalidLatinNameException extends IllegalArgumentException {
    public InvalidLatinNameException(String name) {
        super("The name is not in latin name form: " + name + "\nThe correct format is <genre species>");
    }
}
