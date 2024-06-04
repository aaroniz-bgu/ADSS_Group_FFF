package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.*;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity(name="Category")
public class Category implements Serializable {

    @Id
    private String categoryName;

    @Column
    private int level;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "category_hierarchy",
            joinColumns = @JoinColumn(name = "parent"),
            inverseJoinColumns = @JoinColumn(name = "child")
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

    public Category(String categoryName, int level, List<Category> children, List<Product> products) {
        this.categoryName = categoryName;
        this.level = level;
        this.children = children;
        this.products = products;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
