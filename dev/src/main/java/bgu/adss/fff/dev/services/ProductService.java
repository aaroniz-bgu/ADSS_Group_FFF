package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Product;

public interface ProductService {

    Product createProduct(Product product);
    Product getProduct(long id);
    Product updateProduct(Product product);
    void deleteProduct(long id);
    Product addStock(long id, int quantity, String expirationDate);

}
