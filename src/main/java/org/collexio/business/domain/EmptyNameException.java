package org.collexio.business.domain;

public class EmptyNameException extends IllegalArgumentException {
    public EmptyNameException() {
      super("Name can't be empty");
    }
}
