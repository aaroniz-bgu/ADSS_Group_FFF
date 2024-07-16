package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.*;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branchID")
    private Branch branch;

    public Item() {
    }

    /**
     * Item constructor
     * @param itemID item id
     * @param expirationDate item expiration date
     * @param isDefected is item defected
     * @param branch item branch
     */
    public Item(long itemID, LocalDate expirationDate, boolean isDefected, Branch branch) {
        this.itemID = itemID;
        this.expirationDate = expirationDate;
        this.isDefected = isDefected;
        this.branch = branch;
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

    public void setDefective(boolean isDefected) {
        this.isDefected = isDefected;
    }

    /**
     * Set item id
     * @param itemID item id
     */
    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    /**
     * Get is throwable
     * @return if item is throwable
     */
    public boolean isThrowable() {
        return isDefected || expirationDate.isBefore(LocalDate.now());
    }

    /**
     * Get item branch
     * @return item branch
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * Set item branch
     * @param branch item branch
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
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
