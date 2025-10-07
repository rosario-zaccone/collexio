package org.collexio;

public abstract class InanimateItem extends Item{
    private final String creator;

    public InanimateItem(String id, String name, int quantity, String creator) {
        super(id, name, quantity);
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public abstract double getAvgPrice();
}
