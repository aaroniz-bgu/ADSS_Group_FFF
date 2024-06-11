package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.*;
import bgu.adss.fff.dev.controllers.mappers.CategoryMapper;
import bgu.adss.fff.dev.controllers.mappers.DiscountMapper;
import bgu.adss.fff.dev.controllers.mappers.ProductMapper;
import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.domain.models.Discount;
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

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Establish a new category in the system
     * @param categoryDto The request containing the folowing data:
     *                    - {@code String categoryName}: The name of the category
     * @param parent The name of the parent category
     * @return ResponseEntity containing teh created category if successful, or a bad request if the input is invalid
     *
     *
     */
    @PostMapping("/{parent}")
    public ResponseEntity<?> createCategory(@RequestBody RequestCategoryDto categoryDto, @PathVariable("parent") String parent) {
        Category category = CategoryMapper.map(categoryDto);
        CategoryDto response = map(categoryService.createCategory(category, parent));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Fetches a category by its name
     * @param name The name of the category
     * @return ResponseEntity containing the category if found, or no content if not found
     */
    @GetMapping("/{name}")
    public ResponseEntity<?> getCategory(@PathVariable("name") String name) {
        CategoryDto category = map(categoryService.getCategory(name));
        return ResponseEntity.ok(category);
    }

    /**
     * Fetches all categories in the system
     * @return ResponseEntity containing all categories in the system
     */
    @GetMapping
    public ResponseEntity<CategoryDto[]> getCategories(){
        return ResponseEntity.ok(CategoryMapper.map(categoryService.getCategories()));
    }

    /**
     * Updates a category in the system
     * @param name The name of the category
     * @param request The request containing the following data:
     *             - {@code String categoryName}: The name of the category
     *             - {@code int level}: The level of the category
     *             - {@code CategoryDto[] children}: The children of the category
     *             - {@code ProductDto[] products}: The products in the category
     * @return ResponseEntity containing the updated category if successful, or a bad request if the input is invalid
     */
    @PutMapping("/{name}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String name, @RequestBody CategoryDto request){
        Category category = categoryService.updateCategory(name, map(request));
        CategoryDto categoryDto = CategoryMapper.map(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    /**
     * Deletes a category associated with the given name from the system
     * @param name The name of the category
     * @return ResponseEntity containing no content
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteCategory(@PathVariable ("name") String name){
        categoryService.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds a discount to a category
     * @param name The name of the category
     * @param childrenDto The request containing the following data:
     *                  - {@code CategoryDto[] children}: The children of the category
     * @return ResponseEntity containing the updated category if successful, or a bad request if the input is invalid
     */
    @PutMapping("/children/{name}")
    public ResponseEntity<?> updateCategory(@PathVariable("name") String name,
                                           @RequestBody CategoryDto[] childrenDto) {
        List<Category> children = CategoryMapper.map(childrenDto);
        Category category = categoryService.updateChildren(name, children);
        CategoryDto categoryDto = CategoryMapper.map(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    /**
     * Adds products to a category
     * @param name The name of the category
     * @param productsDto The request containing the following data:
     *                    - {@code ProductDto[] products}: The products to add to the category
     * @return ResponseEntity containing the updated category if successful, or a bad request if the input is invalid
     */
    @PutMapping("/products/{name}")
    public ResponseEntity<?> updateProduct(@PathVariable("name") String name,
                                           @RequestBody ProductDto[] productsDto) {
        List<Product> products = ProductMapper.map(productsDto);
        Category category = categoryService.updateProducts(name, products);
        CategoryDto categoryDto = CategoryMapper.map(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    /**
     * Adds a discount to a category
     * @param name The name of the category
     * @param discountDto The request containing the following data:
     *                    - {@code float discountPercentage}: The discount percentage
     *                    - {@code LocalDateTime startDate}: The start date of the discount
     *                    - {@code LocalDateTime endDate}: The end date of the discount
     * @return ResponseEntity containing no content
     */
    @PutMapping("/discount/{name}")
    public ResponseEntity<?> addCategoryDiscount(@PathVariable("name") String name, @RequestBody DiscountDto discountDto) {
        Discount discount = DiscountMapper.map(discountDto);
        categoryService.addCategoryDiscount(name, discount);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds a product to a category
     * @param categories The request containing the following data:
     *                    - {@code CategoryDto[] categories}: The categories to add the product to
     * @param id The id of the product
     * @return ResponseEntity containing no content
     */
    @PutMapping("/product/{id}")
    public ResponseEntity<?> addProduct(@RequestBody RequestCategoriesDto categories, @PathVariable("id") long id) {
        categoryService.addProduct(id, categories.categories());
        return ResponseEntity.noContent().build();
    }

}