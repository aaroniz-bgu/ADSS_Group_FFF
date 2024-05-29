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

}
