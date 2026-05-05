package org.collexio.business.domain;

import org.collexio.persistence.entity.ItemEntity;
import org.collexio.persistence.entity.ItemStatus;

import java.util.*;

public class Item {
    private final Long id;
    private ItemStatus status;
    private ItemPhoto photo;
    private ItemSpec spec;
    private final List<Transaction> transactions = new ArrayList<>();


    public Item(Long id, ItemStatus status, ItemPhoto photo, ItemSpec spec) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("Id must be positive or null");
        this.id = id;
        this.status = status;
        this.photo = photo == null ? null : new ItemPhoto(photo);
        this.spec = spec;
    }

    public Item(ItemStatus status, ItemSpec spec) {
        this(null, status, null, spec);
    }


    public Item(ItemStatus status, ItemPhoto photo, ItemSpec spec) {
        this(null, status, photo, spec);
    }

    public Item(Item item)  {
        this.id = item.id;
        this.status = item.status;
        this.photo = item.photo == null ? null : new ItemPhoto(item.photo);
        this.spec = item.spec;
        item.transactions.forEach(this::addTransaction);
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public void setPhoto(ItemPhoto photo) {
        this.photo = new ItemPhoto(photo);
    }

    public void setSpec(ItemSpec spec) {
        this.spec = spec;
    }

    public Long getId() {
        return id;
    }


    public ItemStatus getStatus() {
        return status;
    }


    public ItemPhoto getPhoto() {
        return new ItemPhoto(photo);
    }


    public ItemSpec getSpec() {
        return spec;
    }


    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }


    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }


    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public double balance() {
        return transactions.stream()
                .map(e -> e.isIncome() ? e.getAmount() : -e.getAmount())
                .reduce(0.0, Double::sum);
    }

    public boolean isAvailable() {
        return transactions.stream().filter(e -> e.isIncome()).count() > 0; // no sell transaction
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Item{");
        sb.append("id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", photo=").append(photo);
        sb.append(", details=").append(spec);
        sb.append(", transactions=").append(transactions);
        sb.append('}');
        return sb.toString();
    }

    public static Item fromEntity(ItemEntity entity) {
        Item item = new Item(
                entity.getId(),
                entity.getStatus(),
                entity.getPhoto() == null ? null : ItemPhoto.fromEntity(entity.getPhoto()),
                entity.getSpec() == null ? null : ItemSpec.fromEntity(entity.getSpec())
                );
        entity.getTransactions().forEach(t -> item.addTransaction(Transaction.fromEntity(t)));
        return item;
    }

    public ItemEntity toEntity() {
        ItemEntity entity = new ItemEntity(
                this.id,
                this.status,
                this.photo == null ? null : this.photo.toEntity(),
                this.spec == null ? null : this.spec.toEntity()
        );
        this.transactions.forEach(t -> entity.addTransaction(t.toEntity()));
        return entity;
    }
}
