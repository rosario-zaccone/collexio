package org.collexio.persistence.entity;

import java.util.Objects;

public class ItemSpecEntity {
    private final Long id;
    private final ItemType type;
    private final String name;
    private final String description;


    public ItemSpecEntity(Long id, ItemType type, String name, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public ItemSpecEntity(Long id, ItemType type, String name) {
        this(id, type, name, "no description");
    }

    public ItemSpecEntity(ItemType type, String name, String description) {
        this(null, type, name, description);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemSpecEntity that = (ItemSpecEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ItemSpecEntity{");
        sb.append("id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String toStringNoId() {
        final StringBuilder sb = new StringBuilder("ItemSpecEntity{");
        sb.append("type=").append(type);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
