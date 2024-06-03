package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.ItemRepository;
import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Item;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
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

    // Additional operations

    @Override
    public Product updateStorage(long id, List<Item> storage) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found");
        }

        Product product = getProductByID(id);

        // Delete all items from storage
        itemRepository.deleteAll(product.getStorage());
        product.setStorage(new LinkedList<>());

        // Fill with new items
        for (Item item : storage) {
            product.addToStorage(item);
            save(item);
        }

        return save(product);
    }

    @Override
    public Product updateShelves(long id, List<Item> shelves) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found");
        }

        Product product = getProductByID(id);

        // Delete all items from storage
        itemRepository.deleteAll(product.getShelves());
        product.setShelves(new LinkedList<>());

        // Fill with new items
        for (Item item : shelves) {
            product.addToShelves(item);
            save(item);
        }

        return save(product);
    }
}
