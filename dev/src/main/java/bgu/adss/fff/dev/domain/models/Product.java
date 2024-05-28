package bgu.adss.fff.dev.domain.models;

import java.io.Serializable;
import java.util.Map;

public class Product implements Serializable {

    private long productID;

    private String productName;
    private float price;
    private Discount discount;
    private Map<Long, Item> shelves;
    private Map<Long, Item> storage;
    private int minimalQuantity;
}
