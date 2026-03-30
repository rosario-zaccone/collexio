package org.collexio.business.domain;

import org.collexio.persistence.model.ItemCollectionEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItemCollection {
    private final Long id;
    private String name;
    private final List<Item> data;

    public ItemCollection(Long id, String name) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("Id must be positive or null");
        if (name.isEmpty())
            throw new IllegalArgumentException("Name can't be empty");
        this.id = id;
        this.name = name;
        this.data = new ArrayList<>();
    }

    public ItemCollection(String name) {
        this(null, name);
    }

    public void setName(String name) {
        if (name.isEmpty())
            throw new IllegalArgumentException("Name can't be empty");
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Item> getData() {
        return Collections.unmodifiableList(this.data);
    }

    public void addItem(Item item) {
        data.add(new Item(item));
    }

    public void removeItem(Item item) {
        data.remove(item);
    }

    public int totalQuantity() {
        return data.size();
    }

    public int length() {
        return data.size();
    }

    public double totalBalance() {
        return data.stream()
                .map(Item::balance)
                .reduce(0.0, Double::sum);
    }

    @Override
    public String toString() {
        return "ItemCollection{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", data=" + data +
                '}';
    }

    public static ItemCollection fromEntity(ItemCollectionEntity entity) {
        ItemCollection collection = new ItemCollection(entity.getId(), entity.getName());
        entity.getData().forEach(i -> collection.addItem(Item.fromEntity(i)));
        return collection;
    }

    public ItemCollectionEntity toEntity() {
        ItemCollectionEntity entity = new ItemCollectionEntity(this.id, this.name);
        this.data.forEach(i -> entity.addItem(i.toEntity()));
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemCollection that = (ItemCollection) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
