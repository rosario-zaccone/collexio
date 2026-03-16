package org.collexio.persistence.model;

import org.collexio.business.domain.ItemPhoto;
import org.collexio.business.domain.Transaction;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class ItemEntity {
    private final Long id;
    private final ItemType type;
    private final String name;
    private final int quantity;
    private final ItemPhoto photo;
    private final String description;

    private final Set<Transaction> transactions = new TreeSet<>();


    public ItemEntity(Long id, ItemType type, String name, int quantity, ItemPhoto photo, String description) {
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

    public ItemPhoto getPhoto() {
        return photo;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Set<Transaction> getTransactions() {
        return Collections.unmodifiableSet(transactions);
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
}
