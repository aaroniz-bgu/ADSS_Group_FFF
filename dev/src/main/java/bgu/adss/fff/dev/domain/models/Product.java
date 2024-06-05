package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity(name="Product")
public class Product implements Serializable {

    @Id
    private long productID;

    @Column
    private String productName;

    @Column
    private float price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_discount",
            joinColumns = @JoinColumn(name = "productID"),
            inverseJoinColumns = @JoinColumn(name = "discountID")
    )
    private Discount discount;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "shelves_items",
            joinColumns = @JoinColumn(name = "productID"),
            inverseJoinColumns = @JoinColumn(name = "itemID")
    )
    private List<Item> shelves;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "storage_items",
            joinColumns = @JoinColumn(name = "productID"),
            inverseJoinColumns = @JoinColumn(name = "itemID")
    )
    private List<Item> storage;

    @Column
    private int minimalQuantity;

    public Product(long productID, String productName, float price, Discount discount, List<Item> shelves, List<Item> storage, int minimalQuantity) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.discount = discount;
        this.shelves = shelves;
        this.storage = storage;
        this.minimalQuantity = minimalQuantity;
    }

    public Product() {

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

    public List<Item> getShelves() {
        return shelves;
    }

    public void setShelves(List<Item> shelves) {
        this.shelves = shelves;
    }

    public List<Item> getStorage() {
        return storage;
    }

    public void setStorage(List<Item> storage) {
        this.storage = storage;
    }

    public int getMinimalQuantity() {
        return minimalQuantity;
    }

    public void setMinimalQuantity(int minimalQuantity) {
        this.minimalQuantity = minimalQuantity;
    }

    public void addToStorage(Item item) {
        storage.add(item);
    }

    public void addToShelves(Item item) {
        shelves.add(item);
    }

    public int getQuantity() {
        return getStorageQuantity() + getShelvesQuantity();
    }

    public int getStorageQuantity() {
        return storage.size();
    }

    public int getShelvesQuantity() {
        return shelves.size();
    }

    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", shelves=" + shelves +
                ", storage=" + storage +
                ", minimalQuantity=" + minimalQuantity +
                '}';
    }
}
