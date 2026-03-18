package org.collexio.persistence.model;

import java.util.*;

public class ItemEntity {
    private final Long id;
    private final ItemType type;
    private final String name;
    private final int quantity;
    private final ItemPhotoEntity photo;
    private final String description;

    private final List<TransactionEntity> transactions = new ArrayList<>();


    public ItemEntity(Long id, ItemType type, String name, int quantity, ItemPhotoEntity photo, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.quantity = quantity;
        this.photo = photo;
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

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public ItemPhotoEntity getPhoto() {
        return photo;
    }

    public void addTransaction(TransactionEntity transaction) {
        transactions.add(transaction);
    }

    public List<TransactionEntity> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemEntity that = (ItemEntity) o;
        return quantity == that.quantity && Objects.equals(id, that.id) && type == that.type && Objects.equals(name, that.name) && Objects.equals(photo, that.photo) && Objects.equals(description, that.description) && Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, quantity, photo, description, transactions);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ItemEntity{");
        sb.append("id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", name='").append(name).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", photo=").append(photo);
        sb.append(", description='").append(description).append('\'');
        sb.append(", transactions=").append(transactions);
        sb.append('}');
        return sb.toString();
    }

    public String toStringNoId() {
        final StringBuilder sb = new StringBuilder("ItemEntity{");
        sb.append("type=").append(type);
        sb.append(", name='").append(name).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", photo=").append(photo);
        sb.append(", description='").append(description).append('\'');
        sb.append(", transactions=").append(transactions);
        sb.append('}');
        return sb.toString();
    }
}
