package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.ItemRepository;
import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Item;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Item> addItems(long id, List<Item> items) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found");
        }

        if (items == null || items.isEmpty()) {
            throw new ProductException("Items list is empty");
        }

        Product product = getProductByID(id);

        List<Item> savedItems = new LinkedList<>();
        for (Item item : items) {
            item.setItemID(generateRandomItemID());
            item = save(item);

            product.addToStorage(item);
            savedItems.add(item);
        }
        save(product);

        return savedItems;
    }

    @Override
    public List<Item> moveToShelves(long id, int amount) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found");
        }

        if (amount <= 0) {
            throw new ProductException("Amount must be greater than 0");
        }

        Product product = getProductByID(id);

        List<Item> storage = product.getStorage();
        List<Item> shelves = product.getShelves();

        if (storage.size() < amount) {
            throw new ProductException("Not enough items in storage");
        }

        List<Item> movedItems = new LinkedList<>();
        for (int i = 0; i < amount; i++) {
            Item item = storage.remove(0);
            shelves.add(item);

            movedItems.add(item);
        }

        product.setStorage(storage);
        product.setShelves(shelves);
        save(product);

        return movedItems;
    }

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
