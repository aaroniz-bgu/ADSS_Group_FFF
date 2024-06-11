package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.*;


import java.io.Serializable;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "category_products",
            joinColumns = @JoinColumn(name = "categoryID"),
            inverseJoinColumns = @JoinColumn(name = "productID")
    )
    private List<Product> products;

    public Category() {}

    /**
     * Category constructor
     * @param categoryName category name
     * @param level category level
     * @param children category children
     * @param products category products
     */
    public Category(String categoryName, int level, List<Category> children, List<Product> products) {
        this.categoryName = categoryName;
        this.level = level;
        this.children = children;
        this.products = products;
    }

    /**
     * Add a child category
     * @return child category
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * get category name
     * @return category level
     */
    public int getLevel() {
        return level;
    }

    /**
     * set category level
     * @param level category level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * get category children
     * @return list of children
     */
    public List<Category> getChildren() {
        return children;
    }

    /**
     * set category children
     * @param children list of children
     */
    public void setChildren(List<Category> children) {
        this.children = children;
    }

    /**
     * get category products
     * @return list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * set category products
     * @param products list of products
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
