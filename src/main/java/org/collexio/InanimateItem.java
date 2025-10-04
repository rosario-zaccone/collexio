package org.collexio;

public abstract class InanimateItem extends Item{
    private String creator;

    public InanimateItem(String id, String name, int quantity) {
        super(id, name, quantity);
    }

    public abstract double getAvgPrice();
}
