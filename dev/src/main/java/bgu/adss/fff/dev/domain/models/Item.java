package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDate;

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

    /**
     * Item constructor
     * @param itemID item id
     * @param expirationDate item expiration date
     * @param isDefected is item defected
     */
    public Item(long itemID, LocalDate expirationDate, boolean isDefected) {
        this.itemID = itemID;
        this.expirationDate = expirationDate;
        this.isDefected = isDefected;
    }

    /**
     * Get item id
     * @return item id
     */
    public long getItemID() {
        return itemID;
    }

    /**
     * Get item expiration date
     * @return item expiration date
     */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Get if item is defected
     * @return if item is defected
     */
    public boolean isDefected() {
        return isDefected;
    }

    /**
     * Set item id
     * @param itemID item id
     */
    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

//    public boolean isOutOfStock() {
//        return isDefected || expirationDate.isBefore(LocalDate.now());
//    }

    @Override
    public String toString() {
        return "Item{" +
                "itemID=" + itemID +
                ", expirationDate=" + expirationDate +
                ", isDefected=" + isDefected +
                '}';
    }
}
