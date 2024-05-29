package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


import java.io.Serializable;
import java.util.Map;

@Entity(name="Category")
public class Category implements Serializable {

    @Id
    private long categoryID;

    @Column
    private String categoryName;

    @Column
    private Map<String, Category> children;

    @Column
    private Map<String, Product> products;

    public Category() {}

    public Category(long categoryID, String categoryName, Map<String, Category> children, Map<String, Product> products) {
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

    public Map<String, Category> getChildren() {
        return children;
    }

    public void setChildren(Map<String, Category> children) {
        this.children = children;
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        products.put(product.getProductName(), product);
    }

    public void removeProduct(Product product) {
        products.remove(product.getProductName());
    }

    public void addSubCategory(Category category) {
        children.put(category.getCategoryName(), category);
    }

}
