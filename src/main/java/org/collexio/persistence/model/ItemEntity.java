package org.collexio.persistence.model;

import org.collexio.business.domain.Transaction;

import java.util.*;

public class ItemEntity {
    private final Long id;
    private final ItemStatus status;
    private final ItemPhotoEntity photo;
    private final ItemSpecEntity details;
    private final List<TransactionEntity> transactions = new ArrayList<>();


    public ItemEntity(Long id, ItemStatus status, ItemPhotoEntity photo, ItemSpecEntity details) {
        this.id = id;
        this.status = status;
        this.photo = photo;
        this.details = details;
    }


    public ItemEntity(ItemStatus status, ItemPhotoEntity photo, ItemSpecEntity details) {
        this(null, status, photo, details);
    }


    public Long getId() {
        return id;
    }


    public ItemStatus getStatus() {
        return status;
    }


    public ItemPhotoEntity getPhoto() {
        return photo;
    }


    public ItemSpecEntity getDetails() {
        return details;
    }


    public List<TransactionEntity> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }


    public void addTransaction(TransactionEntity transaction) {
        transactions.add(transaction);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemEntity that = (ItemEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ItemEntity{");
        sb.append("id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", photo=").append(photo);
        sb.append(", details=").append(details);
        sb.append(", transactions=").append(transactions);
        sb.append('}');
        return sb.toString();
    }

    public String toStringNoId() {
        final StringBuilder sb = new StringBuilder("ItemEntity{");
        var transactionsNoId = transactions.stream().map(TransactionEntity::toStringNoId).toList();
        sb.append("status=").append(status);
        sb.append(", photo=").append(photo.toStringNoId());
        sb.append(", details=").append(details.toStringNoId());
        sb.append(", transactions=").append(transactionsNoId);
        sb.append('}');
        return sb.toString();
    }

    public void removeTransaction(TransactionEntity transaction) {
        transactions.remove(transaction);
    }
}
