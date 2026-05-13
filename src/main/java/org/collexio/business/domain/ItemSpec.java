package org.collexio.business.domain;

import org.collexio.persistence.entity.ItemSpecEntity;
import org.collexio.persistence.entity.ItemType;

import java.util.Objects;

public class ItemSpec {
    private final Long id;
    private final ItemType type;
    private final String name;
    private final String description;


    public ItemSpec(Long id, ItemType type, String name, String description) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("Id must be positive or null");
        if (name.isEmpty())
            throw new EmptyNameException();
        if (type == ItemType.PLANT && !isValidLatinName(name)) {
            throw new InvalidLatinNameException(name);
        }
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public ItemSpec(Long id, ItemType type, String name) {
        this(id, type, name, "no description");
    }

    public ItemSpec(ItemType type, String name, String description) {
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
    public String toString() {
        final StringBuilder sb = new StringBuilder("ItemSpec{");
        sb.append("id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static ItemSpec fromEntity(ItemSpecEntity entity) {
        return new ItemSpec(entity.getId(), entity.getType(), entity.getName(), entity.getDescription());
    }

    public ItemSpecEntity toEntity() {
        return new ItemSpecEntity(this.id, this.type, this.name, this.description);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemSpec itemSpec = (ItemSpec) o;
        return Objects.equals(id, itemSpec.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private boolean isValidLatinName(String name) {
        return name != null && name.matches("[A-Za-z]+ [A-Za-z]+");
    }
}
