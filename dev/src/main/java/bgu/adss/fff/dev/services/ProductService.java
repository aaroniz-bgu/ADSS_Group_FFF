package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.domain.models.Item;
import bgu.adss.fff.dev.domain.models.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);
    Product getProduct(long id);
    Product updateProduct(Product product);
    void deleteProduct(long id);

    Product updateStorage(long id, List<Item> storage);
    Product updateShelves(long id, List<Item> shelves);

    List<Item> addItems(long id, List<Item> items);
    List<Item> moveToShelves(long id, int amount);
    Discount addDiscount(long id, Discount discount);

}
