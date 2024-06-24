package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.*;
import bgu.adss.fff.dev.controllers.mappers.DiscountMapper;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.domain.models.Item;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static bgu.adss.fff.dev.controllers.mappers.ProductMapper.map;
import static bgu.adss.fff.dev.controllers.mappers.ItemMapper.map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    // Basic CRUD operations

    /**
     * Establishes a new product in the system.
     * @param request The request containing the product details:<br>
     *                - {@code long productID}: The product's unique identifier.<br>
     *                - {@code String productName}: The product's name.<br>
     *                - {@code float price}: The product's price.<br>
     *                - {@code int minimalQuantity}: The minimal quantity of the product to be kept in stock.<br>
     * @return ResponseEntity containing the created product if successful, or an error message if not.
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody RequestProductDto request) {
        Product product = service.createProduct(map(request));
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    /**
     * Fetches product by its unique identifier.
     * @param id The product's unique identifier.
     * @return ResponseEntity containing the product if found, or an error message if not.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") long id){
        Product product = service.getProduct(id);
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    /**
     * Updates product that is already in the system.
     * @param request The request containing the product details:<br>
     *                - {@code long productID}: The product's unique identifier.<br>
     *                - {@code String productName}: The product's name.<br>
     *                - {@code float price}: The product's price.<br>
     *                - {@code ItemDto[] storage}: The product's storage.<br>
     *                - {@code ItemDto[] shelves}: The product's shelves.<br>
     *                - {@code int minimalQuantity}: The minimal quantity of the product to be kept in stock.<br>
     * @return ResponseEntity containing the updated product if successful, or an error message if not.
     */
    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto request) {
        Product product = service.updateProduct(map(request));
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    /**
     * Deletes product by its unique identifier.
     * @param id The product's unique identifier.
     * @return an empty ResponseEntity if successful, or an error message if not.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") long id) {
        service.deleteProduct(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    // Additional operations

    /**
     * Adds items to the product's storage.
     * @param id The product's unique identifier.
     * @param items The items to be added to the storage.
     *              - {@code String expirationDate}: The item's expiration date.
     *              - {@code boolean isDefected}: Whether the item is defected.
     *              - {@code int amount}: The amount of the item.
     * @return ResponseEntity containing the created items if successful, or an error message if not.
     */
    @PostMapping("/item/{id}")
    public ResponseEntity<?> addItems(@PathVariable("id") long id,
                                      @RequestBody RequestItemDto items) {
        List<Item> itemsList = map(items);
        itemsList = service.addItems(id, itemsList);
        ItemDto[] itemDTOs = map(itemsList);
        return new ResponseEntity<>(itemDTOs, HttpStatus.OK);
    }

    /**
     * Moves items from the product's storage to its shelves.
     * @param id The product's unique identifier.
     * @param amount An object containing the amount of items to move.
     * @return ResponseEntity containing the moved items if successful, or an error message if not.
     */
    @PutMapping("/item/{id}")
    public ResponseEntity<?> moveToShelves(@PathVariable("id") long id,
                                           @RequestBody RequestAmountDto amount) {
        List<Item> itemsList = service.moveToShelves(id, amount.amount());
        ItemDto[] itemDTOs = map(itemsList);
        return new ResponseEntity<>(itemDTOs, HttpStatus.OK);
    }

    /**
     * Sets an item as defective.
     * @param id The product's unique identifier.
     * @param item The item to update.
     * @return an empty ResponseEntity if successful, or an error message if not.
     */
    @PutMapping("/item/defective/{id}")
    public ResponseEntity<?> setItemDefective(@PathVariable("id") long id,
                                              @RequestBody ItemDto item) {
        service.setItemDefective(id, item.itemID(), item.isDefected());
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds discount to the product.
     * @param id The product's unique identifier.
     * @param discountDto The discount details (The discount needs to be created first):<br>
     *                    - {@code long discountID}: The discount unique identifier.<br>
     *                    - {@code String startDate}: The discount start date.<br>
     *                    - {@code String endDate}: The discount end date.<br>
     *                    - {@code float discountPercent}: The discount percentage.
     * @return an empty ResponseEntity if successful, or an error message if not.
     */
    @PutMapping("/discount/{id}")
    public ResponseEntity<?> addDiscount(@PathVariable("id") long id, @RequestBody DiscountDto discountDto) {
        Discount discount = DiscountMapper.map(discountDto);
        service.addDiscount(id, discount);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the storage with an updated set of items.
     * @param id The product's unique identifier.
     * @param items The updated items to be stored (They need to be created first):<br>
     *              - {@code long itemID}: The item's unique identifier.<br>
     *              - {@code String expirationDate}: The item's expiration date.<br>
     *              - {@code boolean isDefected}: Whether the item is defected.
     * @return ResponseEntity containing the updated product if successful, or an error message if not.
     */
    @PutMapping("/storage/{id}")
    public ResponseEntity<?> updateStorage(@PathVariable("id") long id,
                                           @RequestBody ItemDto[] items) {
        List<Item> storage = map(items);
        Product product = service.updateStorage(id, storage);
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    /**
     * Updates the shelves with an updated set of items.
     * @param id The product's unique identifier.
     * @param items The updated items to be stored (They need to be created first):<br>
     *              - {@code long itemID}: The item's unique identifier.<br>
     *              - {@code String expirationDate}: The item's expiration date.<br>
     *              - {@code boolean isDefected}: Whether the item is defected.
     * @return ResponseEntity containing the updated product if successful, or an error message if not.
     */
    @PutMapping("/shelves/{id}")
    public ResponseEntity<?> updateShelves(@PathVariable("id") long id,
                                           @RequestBody ItemDto[] items) {
        List<Item> shelves = map(items);
        Product product = service.updateShelves(id, shelves);
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @PutMapping("/supplier/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable("id") long id,
                                           @RequestBody long supplierID) {
        Product product = service.updateSupplier(id, supplierID);
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }
}
