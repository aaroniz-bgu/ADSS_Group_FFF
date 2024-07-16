package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.contracts.RequestItemDto;
import bgu.adss.fff.dev.controllers.mappers.ItemMapper;
import bgu.adss.fff.dev.data.ItemRepository;
import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.domain.models.Item;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final BranchService branchService;

    /**
     * ProductServiceImpl constructor
     * @param productRepository product repository
     * @param itemRepository item repository
     */
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ItemRepository itemRepository,
                              BranchService branchService) {
        this.productRepository = productRepository;
        this.itemRepository = itemRepository;
        this.branchService = branchService;
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
            // Since the branch in the item, is not an instance from the system but rather one we got from the client
            // we need to access it in the system.
            // If the client is an idiot, and have sent a nonexistent branch name, this function will let him know
            // that he's an idiot (respectfully).
            item.setBranch(branchService.getBranch(item.getBranch().getName()));
            item = save(item);

            product.addToStorage(item);
            savedItems.add(item);
        }

        product.reorderItems();

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
        List<Item> movedItems = product.moveToShelves(amount);

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
            // See the comment @ addItems
            item.setBranch(branchService.getBranch(item.getBranch().getName()));
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
            // See the comment @ addItems
            item.setBranch(branchService.getBranch(item.getBranch().getName()));
            product.addToShelves(item);
            save(item);
        }

        return save(product);
    }

    @Override
    public Product updateSupplier(long id, long supplierID, float price) {
        if (!doesProductExist(id)) {
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
        }

        if (price <= 0) {
            throw new ProductException("Price must be greater than 0", HttpStatus.BAD_REQUEST);
        }

        Product product = getProductByID(id);

        if (product.getSupplierPrice() < price) {
            throw new ProductException("Price must be less than the current supplier price", HttpStatus.BAD_REQUEST);
        }

        product.setSupplierID(supplierID);
        product.setSupplierPrice(price);

        return save(product);
    }

    @Override
    public String sellItems(long id, int amount, Branch branch){

        if(!doesProductExist(id)){
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
        }

        Product product = getProductByID(id);

        if(amount <= 0){
            throw new ProductException("Amount must be greater than 0", HttpStatus.BAD_REQUEST);
        }

        List<Item> shelves = product.getShelves();

        if(shelves.size() < amount){
            throw new ProductException("Not enough items in shelves", HttpStatus.BAD_REQUEST);
        }

        branch = branchService.getBranch(branch.getName());

        String message = "Selling " + amount + " items from product " + id + ".\n";

        float priceBeforeDiscount = amount * product.getPrice();
        float priceAfterDiscount = amount * product.getDiscountedPrice();
        if (priceBeforeDiscount == priceAfterDiscount)
            message += "Total Price: " + priceAfterDiscount + "₪.\n";
        else
            message += "Total Price: " + amount * product.getDiscountedPrice() +
                "₪ (Before Discount: " + amount * product.getPrice() + "₪)" + ".\n";

        List<Item> soldItems = new LinkedList<>();
        for(int i = 0; i < amount; i++){
            Item item = shelves.remove(0);
            soldItems.add(item);
        }

        product.reorderItems();

        if(product.getQuantity() < product.getMinimalQuantity()){
            message += orderItems(id, branch);
        }

        product.setShelves(shelves);
        save(product);

        return message;
    }

    @Override
    public boolean throwItem(long productId, long itemId, Branch branch){

        boolean needToOrder = false;

        if(!doesProductExist(productId)){
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
        }

        // Checks whether the branch exists else will throw an error autonomously:
        branch = branchService.getBranch(branch.getName());

        Product product = getProductByID(productId);

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ProductException("Item not found", HttpStatus.NOT_FOUND));

        if(!product.containsItem(itemId)){
            throw new ProductException("Item not found", HttpStatus.NOT_FOUND);
        }

        if(!item.isThrowable()){
            throw new ProductException("Item cannot be removed since it is not defected nor out-of-date",
                    HttpStatus.BAD_REQUEST);
        }

        product.removeItem(itemId);

        product.reorderItems();

        if (product.getQuantity() < product.getMinimalQuantity()){
            needToOrder = !Objects.equals(orderItems(productId, branch), "");
        }

        save(product);

        return needToOrder ;
    }

    @Override
    public String orderItems(long id, Branch branch){
        if(!doesProductExist(id)){
            throw new ProductException("Product not found", HttpStatus.NOT_FOUND);
        }

        // Checks whether the branch exists else will throw an error autonomously:
        branch = branchService.getBranch(branch.getName());

        Product product = getProductByID(id);

        int shortage = product.getShortage();
        if(shortage <= 0){
            return "";
        }

        notifySupplier(id, shortage, branch.getName());
        return "WARNING: Product " + product.getProductName() +
                " is at shortage - Ordered " + shortage + " items from supplier.";
    }

    public String orderItems(Branch branch){
        List<Product> products = productRepository.findAll();
        StringBuilder message = new StringBuilder();
        message.append("Ordering items from suppliers:\n");

        branch = branchService.getBranch(branch.getName());

        for(Product product : products){
            message.append(orderItems(product.getProductID(), branch)).append("\n");
        }
        return message.toString();
    }

    // TODO: Replace with actual supplier notification
    private void notifySupplier(long id, int amount, String branch){
        String expirationDate = LocalDate.now().plusDays(
                4 + (long)(Math.random() * 10)
        ).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        RequestItemDto requestItemDto = new RequestItemDto(expirationDate, false, amount, branch);
        List<Item> items = ItemMapper.map(requestItemDto);
        addItems(id, items);
    }

}
