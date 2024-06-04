package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.contracts.ItemDto;
import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.contracts.RequestCategoryDto;
import bgu.adss.fff.dev.controllers.mappers.CategoryMapper;
import bgu.adss.fff.dev.controllers.mappers.ItemMapper;
import bgu.adss.fff.dev.controllers.mappers.ProductMapper;
import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.domain.models.Item;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static bgu.adss.fff.dev.controllers.mappers.CategoryMapper.map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service){
        this.service = service;
    }

    @PostMapping("/{parent}")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody RequestCategoryDto categoryDto, @PathVariable("parent") String parent){
        Category category = CategoryMapper.map(categoryDto);
        return new ResponseEntity<>(map(service.createCategory(category, parent)), HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("name") String name){
        return ResponseEntity.ok(map(service.getCategory(name)));
    }

    @GetMapping
    public ResponseEntity<CategoryDto[]> getCategories(){
        return ResponseEntity.ok(CategoryMapper.map(service.getCategories()));
    }

    @PutMapping("/{name}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String name, @RequestBody CategoryDto request){
        Category category = service.updateCategory(name, map(request));
        CategoryDto categoryDto = CategoryMapper.map(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteCategory(@PathVariable ("name") String name){
        service.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/children/{name}")
    public ResponseEntity<?> updateCategory(@PathVariable("name") String name,
                                           @RequestBody CategoryDto[] childrenDto) {
        List<Category> children = CategoryMapper.map(childrenDto);
        Category category = service.updateChildren(name, children);
        CategoryDto categoryDto = CategoryMapper.map(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @PutMapping("/products/{name}")
    public ResponseEntity<?> updateProduct(@PathVariable("name") String name,
                                           @RequestBody ProductDto[] productsDto) {
        List<Product> products = ProductMapper.map(productsDto);
        Category category = service.updateProducts(name, products);
        CategoryDto categoryDto = CategoryMapper.map(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

}
