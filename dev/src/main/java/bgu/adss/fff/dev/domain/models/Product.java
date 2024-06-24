package bgu.adss.fff.dev.domain.models;

import bgu.adss.fff.dev.exceptions.ProductException;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
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

    @Column
    private long supplierID;

    /**
     * Product constructor
     * @param productID product id
     * @param productName product name
     * @param price product price
     * @param discount product discount
     * @param shelves product shelves
     * @param storage product storage
     * @param minimalQuantity product minimal quantity
     * @param supplierID supplier id
     */
    public Product(
            long productID, String productName, float price, Discount discount,
            List<Item> shelves, List<Item> storage, int minimalQuantity, long supplierID) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.discount = discount;
        this.shelves = shelves;
        this.storage = storage;
        this.minimalQuantity = minimalQuantity;
        this.supplierID = supplierID;
    }

    public Product() {

    }

    /**
     * Get product id
     * @return product id
     */
    public long getProductID() {
        return productID;
    }

    /**
     * Get product name
     * @return product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Get product price
     * @return product price
     */
    public float getPrice() {
        return price;
    }

    /**
     * Get product discount
     * @return product discount
     */
    public Discount getDiscount() {
        return discount;
    }

    /**
     * Set product discount
     * @param discount product discount
     */
    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    /**
     * Get items on shelves
     * @return list of items on shelves
     **/
    public List<Item> getShelves() {
        return shelves;
    }

    /**
     * Set items on shelves
     * @param shelves list of items to put on shelves
     */
    public void setShelves(List<Item> shelves) {
        this.shelves = shelves;
    }

    /**
     * Get items in storage
     * @return list of items in storage
     */
    public List<Item> getStorage() {
        return storage;
    }

    /**
     * Set items in storage
     * @param storage list of items to put in storage
     */
    public void setStorage(List<Item> storage) {
        this.storage = storage;
    }

    /**
     * Get product minimal quantity
     * @return product minimal quantity
     */
    public int getMinimalQuantity() {
        return minimalQuantity;
    }

    /**
     * Add an item to storage
     * @param item item to add to storage
     */
    public void addToStorage(Item item) {
        storage.add(item);
    }

    /**
     * Add an item to shelves
     * @param item item to add to shelves
     */
    public void addToShelves(Item item) {
        shelves.add(item);
    }

    /**
     * Get total quantity of items from storage and shelves
     * @return total quantity of items
     */
    public int getQuantity() {
        return getStorageQuantity() + getShelvesQuantity();
    }

    /**
     * Get quantity of items in storage
     * @return quantity of items in storage
     */
    public int getStorageQuantity() {
        return storage.size();
    }

    /**
     * Get quantity of items on shelves
     * @return quantity of items on shelves
     */
    public int getShelvesQuantity() {
        return shelves.size();
    }

    /**
     * Get the supplier of the product
     * @return supplier id
     */
    public long getSupplierID() { return supplierID; }

    /**
     * Set the supplier of the product
     * @param supplierID supplier id
     */
    public void setSupplierID(long supplierID) { this.supplierID = supplierID; }

    /**
     * Get quantity of items that are defected
     * @param items list of items
     * @return quantity of defected items
     */
    public int defectiveItemsNum(List<Item> items){
        int defectiveItems = 0;
        for(Item item : items){
            if(item.isDefected()){
                defectiveItems++;
            }
        }
        return defectiveItems;
    }

    /**
     * Get quantity of items that are expired
     * @param items list of items
     * @return quantity of expired items
     */
    public int expiredItemsNum(List<Item> items){
        int expiredItems = 0;
        for(Item item : items){
            if(item.getExpirationDate().isBefore(LocalDate.now())){
                expiredItems++;
            }
        }
        return expiredItems;
    }

    public List<Item> moveToShelves(int amount) {
        if (storage.size() < amount) {
            throw new ProductException("Not enough items in storage", HttpStatus.BAD_REQUEST);
        }

        List<Item> movedItems = new LinkedList<>();
        for (int i = 0; i < amount; i++) {
            Item item = storage.remove(0);
            shelves.add(item);

            movedItems.add(item);
        }

        return movedItems;
    }

    public boolean containsItem(long itemID) {
        for (Item item : shelves) {
            if (item.getItemID() == itemID) {
                return true;
            }
        }

        for (Item item : storage) {
            if (item.getItemID() == itemID) {
                return true;
            }
        }

        return false;
    }

    public Item getItem(long itemID) {
        for (Item item : shelves) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }

        for (Item item : storage) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }

        throw new ProductException("Item not found", HttpStatus.NOT_FOUND);
    }

    public Item removeItem(long itemID) {
        for (Item item : shelves) {
            if (item.getItemID() == itemID) {
                shelves.remove(item);
                return item;
            }
        }

        for (Item item : storage) {
            if (item.getItemID() == itemID) {
                storage.remove(item);
                return item;
            }
        }

        throw new ProductException("Item not found", HttpStatus.NOT_FOUND);
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
                ", supplierID=" +
                '}';
    }
}
