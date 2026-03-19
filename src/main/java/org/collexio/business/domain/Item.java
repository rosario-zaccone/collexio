package org.collexio.business.domain;

import org.collexio.persistence.model.ItemStatus;

import java.util.*;

public class Item {
    private final Long id;
    private ItemStatus status;
    private ItemPhoto photo;
    private ItemSpec details;
    private final List<Transaction> transactions = new ArrayList<>();


    public Item(Long id, ItemStatus status, ItemPhoto photo, ItemSpec details) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("Id must be positive or null");
        this.id = id;
        this.status = status;
        this.photo = photo;
        this.details = details;
    }


    public Item(ItemStatus status, ItemPhoto photo, ItemSpec details) {
        this(null, status, photo, details);
    }

    public Item copy() {
        Item item =  new Item(this.id, this.status, this.photo, this.details);
        this.transactions.forEach(item::addTransaction);
        return item;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public void setPhoto(ItemPhoto photo) {
        this.photo = photo;
    }

    public void setDetails(ItemSpec details) {
        this.details = details;
    }

    public Long getId() {
        return id;
    }


    public ItemStatus getStatus() {
        return status;
    }


    public ItemPhoto getPhoto() {
        return photo;
    }


    public ItemSpec getDetails() {
        return details;
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
        sb.append(", details=").append(details);
        sb.append(", transactions=").append(transactions);
        sb.append('}');
        return sb.toString();
    }
}
