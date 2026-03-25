package org.collexio.presentation.dto;

import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.model.ItemType;

public class ItemSpecDTO {
    private Long id;
    private ItemType type;
    private String name;
    private String description;

    public ItemSpecDTO(Long id, ItemType type, String name, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public ItemType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ItemSpec toDomain() {
        return new ItemSpec(id, type, name, description);
    }

    public static ItemSpecDTO fromDomain(ItemSpec spec) {
        return new ItemSpecDTO(spec.getId(), spec.getType(), spec.getName(), spec.getDescription());
    }
}
