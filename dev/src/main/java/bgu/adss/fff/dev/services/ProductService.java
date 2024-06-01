package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Product;

public interface ProductService {

    Product createProduct(Product product);
    Product getProduct(long id);
    Product updateProduct(long id, Product product);
    void deleteProduct(long id);

}
