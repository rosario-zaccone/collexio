package org.collexio.persistence.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItemCollectionEntity {
    private final Long id;
    private final String name;
    private final List<ItemEntity> data;

    public ItemCollectionEntity(Long id, String name) {
        this.id = id;
        this.name = name;
        this.data = new ArrayList<>();
    }

    public ItemCollectionEntity(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ItemEntity> getData() {
        return Collections.unmodifiableList(this.data);
    }

    public void addItem(ItemEntity item) {
        data.add(item);
    }

    public void removeItem(ItemEntity item) {
        data.remove(item);
    }

    @Override
    public String toString() {
        return "ItemCollectionEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", data=" + data +
                '}';
    }

    public String toStringNoId() {
        return "ItemCollectionEntity{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCollectionEntity that = (ItemCollectionEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
