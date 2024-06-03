package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;

@Entity(name="Item")
public class Item implements Serializable {

    @Id
    private long itemID;

    @Column
    private LocalDate expirationDate;

    @Column
    private boolean isDefected;

    public Item() {
    }

    public Item(long itemID, LocalDate expirationDate, boolean isDefected) {
        this.itemID = itemID;
        this.expirationDate = expirationDate;
        this.isDefected = isDefected;
    }

    public long getItemID() {
        return itemID;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public boolean isDefected() {
        return isDefected;
    }

    public void setDefected(boolean isDefected) {
        this.isDefected = isDefected;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public boolean isOutOfStock() {
        return isDefected || expirationDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemID=" + itemID +
                ", expirationDate=" + expirationDate +
                ", isDefected=" + isDefected +
                '}';
    }
}
