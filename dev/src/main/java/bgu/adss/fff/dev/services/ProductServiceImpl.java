package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Autowired
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product createProduct(Product product) {
        repository.save(product);
        return product;
    }

    @Override
    public Product getProduct(long id) {
        return repository.findById(id).orElseThrow(ProductException::new);
    }

    @Override
    public Product updateProduct(long id, Product product) {
        return null;
    }

    @Override
    public void deleteProduct(long id) {

    }
}
