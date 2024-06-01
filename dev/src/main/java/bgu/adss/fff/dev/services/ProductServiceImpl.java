package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.ItemRepository;
import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Item;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ItemRepository itemRepository) {
        this.productRepository = productRepository;
        this.itemRepository = itemRepository;
    }

    private Product getProductByID(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductException("Product not found"));
    }

    private boolean doesProductExist(long id) {
        return productRepository.existsById(id);
    }

    private long generateRandomItemID() {
        long id =  new Random().nextLong();

        while (itemRepository.existsById(id)) {
            id = new Random().nextLong();
        }

        return id;
    }

    @Override
    public Product createProduct(Product product) {

        if (product == null) {
            throw new ProductException("Product cannot be null");
        }

        long id = product.getProductID();

        if (doesProductExist(id)) {
            throw new ProductException("Product already exists");
        }

        // TODO: Any other business logic

        return productRepository.save(product);
    }

    @Override
    public Product getProduct(long id) {
        return getProductByID(id);
    }

    @Override
    public Product updateProduct(Product product) {

        if (product == null) {
            throw new ProductException("Product cannot be null");
        }

        long id = product.getProductID();
        if (!doesProductExist(id)) {
            throw new ProductException("Product not found");
        }

        //TODO: Any other business logic

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(long id) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found");
        }

        //TODO: Any other business logic

        productRepository.deleteById(id);
    }

    @Override
    public Product addStock(long id, int quantity, String expirationDate) {

        Product product = getProductByID(id);

        if (expirationDate == null) {
            throw new ProductException("Expiration date cannot be null");
        }

        LocalDate date = LocalDate.parse(expirationDate);

        if (date.isBefore(LocalDate.now())) {
            throw new ProductException("Expiration date must be in the future");
        }

        if (quantity <= 0) {
            throw new ProductException("Quantity must be greater than 0");
        }

        for (int i = 0; i < quantity; i++) {
            Item item = new Item(generateRandomItemID(), date, false);
            product.addToStorage(itemRepository.save(item));
        }

        return productRepository.save(product);
    }
}
