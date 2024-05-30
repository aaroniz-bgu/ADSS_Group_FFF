package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.*;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Entity(name="Category")
public class Category implements Serializable {

    @Id
    private long categoryID;

    @Column
    private String categoryName;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "category_hierarchy",
            joinColumns = @JoinColumn(name = "categoryID"),
            inverseJoinColumns = @JoinColumn(name = "categoryID")
    )
    private List<Category> children;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "category_products",
            joinColumns = @JoinColumn(name = "categoryID"),
            inverseJoinColumns = @JoinColumn(name = "productID")
    )
    private List<Product> products;

    public Category() {}

    public Category(long categoryID, String categoryName, List<Category> children, List<Product> products) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.children = children;
        this.products = products;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
