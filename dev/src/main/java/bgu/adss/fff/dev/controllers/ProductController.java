package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.contracts.RequestProductDto;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static bgu.adss.fff.dev.controllers.mappers.ProductMapper.map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    // Basic CRUD operations

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody RequestProductDto request) {
        Product product = map(request);
        return new ResponseEntity<>(service.createProduct(product), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") long id){
        Product product = service.getProduct(id);
        ProductDto productDto = map(product);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto request) {
        Product product = map(request);
        return new ResponseEntity<>(service.updateProduct(product), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") long id) {
        service.deleteProduct(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    // Additional operations

    @PutMapping("/addStock/{id}")
    public ResponseEntity<?> addStock(@PathVariable("id") long id,
                                      @RequestParam("quantity") int quantity,
                                      @RequestParam("expirationDate") String expirationDate) {
        return new ResponseEntity<>(service.addStock(id, quantity, expirationDate), HttpStatus.OK);
    }
}
