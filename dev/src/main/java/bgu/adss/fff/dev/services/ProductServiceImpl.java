package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.ItemRepository;
import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.domain.models.Item;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;

    /**
     * ProductServiceImpl constructor
     * @param productRepository product repository
     * @param itemRepository item repository
     */
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

    private Product getProductByID(long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ProductException("Product not found", HttpStatus.NOT_FOUND));
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

    /**
     * Create a new product
     * @param product which creat a new product according to it
     * @return created product
     */
    @Override
    public Product createProduct(Product product) {

        if (product == null) {
            throw new ProductException("Product cannot be null", HttpStatus.BAD_REQUEST);
        }

        long id = product.getProductID();

        if (doesProductExist(id)) {
            throw new ProductException("Product already exists", HttpStatus.CONFLICT);
        }

        return save(product);
    }

    /**
     * Get a product by id
     * @param id product id
     * @return product
     */
    @Override
    public Product getProduct(long id) {
        return getProductByID(id);
    }

    /**
     * Update a product
     * @param product product to update
     * @return updated product
     */
    @Override
    public Product updateProduct(Product product) {

        if (product == null) {
            throw new ProductException("Product cannot be null", HttpStatus.BAD_REQUEST);
        }

        long id = product.getProductID();
        if (!doesProductExist(id)) {
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
        }

        return save(product);
    }

    /**
     * Delete a product
     * @param id product id
     */
    @Override
    public void deleteProduct(long id) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
        }

        deleteProductByID(id);
    }

    // Additional operations

    /**
     * Add items to a product
     * @param id product id
     * @param items list of items to add
     * @return list of added items
     */
    @Override
    public List<Item> addItems(long id, List<Item> items) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
        }

        if (items == null || items.isEmpty()) {
            throw new ProductException("Items list is empty", HttpStatus.BAD_REQUEST);
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

    /**
     * Move items from storage to shelves
     * @param id product id
     * @param amount amount of items to move from storage to shelves
     * @return list of moved items
     */
    @Override
    public List<Item> moveToShelves(long id, int amount) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
        }

        if (amount <= 0) {
            throw new ProductException("Amount must be greater than 0", HttpStatus.BAD_REQUEST);
        }

        Product product = getProductByID(id);

        List<Item> storage = product.getStorage();
        List<Item> shelves = product.getShelves();

        if (storage.size() < amount) {
            throw new ProductException("Not enough items in storage", HttpStatus.BAD_REQUEST);
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

    /**
     * Add a discount to a product
     * @param id product id
     * @param discount discount to add
     * @return added discount
     */
    @Override
    public Discount addDiscount(long id, Discount discount) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
        }

        Product product = getProductByID(id);

        product.setDiscount(discount);
        save(product);

        return discount;
    }

    @Override
    public void setItemDefective(long id, long itemID, boolean isDefective) {
        if (!doesProductExist(id)) {
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
        }

        Product product = getProductByID(id);

        List<Item> storage = product.getStorage();
        List<Item> shelves = product.getShelves();

        Item item = Stream.concat(storage.stream(), shelves.stream())
                .filter(i -> i.getItemID() == itemID)
                .findFirst()
                .orElseThrow(() -> new ProductException("Item not found", HttpStatus.NOT_FOUND));

        item.setDefective(isDefective);

        save(item);
    }

    /**
     * Update storage of a product
     * @param id product id
     * @param storage list of items to update storage
     * @return updated product
     */
    @Override
    public Product updateStorage(long id, List<Item> storage) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
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

    /**
     * Update shelves of a product
     * @param id product id
     * @param shelves list of items to update shelves
     * @return updated product
     */
    @Override
    public Product updateShelves(long id, List<Item> shelves) {

        if (!doesProductExist(id)) {
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
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

    @Override
    public Product updateSupplier(long id, long supplierID) {
        if (!doesProductExist(id)) {
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
        }

        Product product = getProductByID(id);
        product.setSupplierID(supplierID);
        return save(product);
    }

}
