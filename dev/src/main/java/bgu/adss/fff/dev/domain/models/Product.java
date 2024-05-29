package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Map;

@Entity(name="Product")
public class Product implements Serializable {

    @Id
    private final long productID;

    @Column
    private String productName;

    @Column
    private float price;

    @Column
    private Discount discount;

    @Column
    private Map<Long, Item> shelves;

    @Column
    private Map<Long, Item> storage;

    @Column
    private int minimalQuantity;

    public Product(long productID, String productName, float price, Discount discount, Map<Long, Item> shelves, Map<Long, Item> storage, int minimalQuantity) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.discount = discount;
        this.shelves = shelves;
        this.storage = storage;
        this.minimalQuantity = minimalQuantity;
    }

    public long getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Map<Long, Item> getShelves() {
        return shelves;
    }

    public void setShelves(Map<Long, Item> shelves) {
        this.shelves = shelves;
    }

    public Map<Long, Item> getStorage() {
        return storage;
    }

    public void setStorage(Map<Long, Item> storage) {
        this.storage = storage;
    }

    public int getMinimalQuantity() {
        return minimalQuantity;
    }

    public void setMinimalQuantity(int minimalQuantity) {
        this.minimalQuantity = minimalQuantity;
    }
}
