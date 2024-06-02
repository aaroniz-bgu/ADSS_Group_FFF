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

    // Helper methods

    private Product save(Product product) {
        return productRepository.save(product);
    }

    private Item save(Item item) {
        return itemRepository.save(item);
    }

    private void deleteProductByID(long id) {
        productRepository.deleteById(id);
    }

    private void deleteItemByID(long id) {
        itemRepository.deleteById(id);
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

    // Basic CRUD operations

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

        return save(product);
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

        return save(product);
    }

    @Override
    public void deleteProduct(long id) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found");
        }

        //TODO: Any other business logic

        deleteProductByID(id);
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
            Item item = save(new Item(generateRandomItemID(), date, false));
            product.addToStorage(item);
        }

        return save(product);
    }

    @Override
    public Product moveToShelves(long id, int quantity) {

        Product product = getProductByID(id);

        if (quantity <= 0) {
            throw new ProductException("Quantity must be greater than 0");
        }

        if (product.getStorage().size() < quantity) {
            throw new ProductException("Not enough items in storage");
        }

        for (int i = 0; i < quantity; i++) {
            Item item = product.getStorage().remove(0);
            product.addToShelves(item);
        }

        return save(product);
    }

    @Override
    public Product removeOutOfStock(long id) {

        Product product = getProductByID(id);

        for (Item item : product.getStorage()) {
            if (item.isOutOfStock()) {
                product.getStorage().remove(item);
                deleteItemByID(item.getItemID());
            }
        }

        for (Item item : product.getShelves()) {
            if (item.isOutOfStock()) {
                product.getShelves().remove(item);
                deleteItemByID(item.getItemID());
            }
        }

        return save(product);
    }
}
