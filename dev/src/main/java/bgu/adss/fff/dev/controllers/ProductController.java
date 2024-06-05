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

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody RequestProductDto request) {
        Product product = service.createProduct(map(request));
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") long id){
        Product product = service.getProduct(id);
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto request) {
        Product product = service.updateProduct(map(request));
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") long id) {
        service.deleteProduct(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    // Additional operations

    @PostMapping("/item/{id}")
    public ResponseEntity<?> addItems(@PathVariable("id") long id,
                                      @RequestBody RequestItemDto items) {
        List<Item> itemsList = map(items);
        itemsList = service.addItems(id, itemsList);
        ItemDto[] itemDTOs = map(itemsList);
        return new ResponseEntity<>(itemDTOs, HttpStatus.OK);
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<?> moveToShelves(@PathVariable("id") long id,
                                           @RequestBody RequestAmountDto amount) {
        List<Item> itemsList = service.moveToShelves(id, amount.amount());
        ItemDto[] itemDTOs = map(itemsList);
        return new ResponseEntity<>(itemDTOs, HttpStatus.OK);
    }

    @PutMapping("/discount/{id}")
    public ResponseEntity<?> addDiscount(@PathVariable("id") long id, @RequestBody DiscountDto discountDto) {
        Discount discount = DiscountMapper.map(discountDto);
        service.addDiscount(id, discount);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/storage/{id}")
    public ResponseEntity<?> updateStorage(@PathVariable("id") long id,
                                           @RequestBody ItemDto[] items) {
        List<Item> storage = map(items);
        Product product = service.updateStorage(id, storage);
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @PutMapping("/shelves/{id}")
    public ResponseEntity<?> updateShelves(@PathVariable("id") long id,
                                           @RequestBody ItemDto[] items) {
        List<Item> shelves = map(items);
        Product product = service.updateShelves(id, shelves);
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }
}
