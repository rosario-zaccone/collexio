package org.collexio.business.domain;

public class Animal {
    private final Long id;
    private final String name;
    private final String scientificName;
    private final int redListLevel;

    public Animal(Long id, String name, String scientificName, int redListLevel) {
        if (id!= null && id <= 0)
            throw new IllegalArgumentException("Id must be positive or null");
        if (redListLevel < 1 || redListLevel > 7)
            throw new IllegalArgumentException("Red list level must be between 1 and 7");
        if (name.isEmpty())
            throw new IllegalArgumentException("Name can't be empty");
        this.id = id;
        this.name = name;
        this.scientificName = scientificName;
        this.redListLevel = redListLevel;
    }

    public Animal(Animal animal) {
        this(animal.getId(), animal.getName(), animal.getScientificName(), animal.getRedListLevel());
    }

    public Animal(String name, String scientificName, int redListLevel) {
        this(null, name, scientificName, redListLevel);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public int getRedListLevel() {
        return redListLevel;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", redListLevel=" + redListLevel +
                '}';
    }
}
