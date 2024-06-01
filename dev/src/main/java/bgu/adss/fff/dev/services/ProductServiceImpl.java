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

        if (product == null) {
            throw new ProductException("Product cannot be null");
        }

        if (repository.existsById(product.getProductID())) {
            throw new ProductException("Product already exists");
        }

        // TODO: Any other business logic

        return repository.save(product);
    }

    @Override
    public Product getProduct(long id) {
        return repository.findById(id).orElseThrow(() -> new ProductException("Product not found"));
    }

    @Override
    public Product updateProduct(Product product) {

        if (product == null) {
            throw new ProductException("Product cannot be null");
        }

        if (!repository.existsById(product.getProductID())) {
            throw new ProductException("Product not found");
        }

        //TODO: Any other business logic

        return repository.save(product);
    }

    @Override
    public void deleteProduct(long id) {

        if (!repository.existsById(id)) {
            throw new ProductException("Product not found");
        }

        //TODO: Any other business logic

        repository.deleteById(id);
    }
}
