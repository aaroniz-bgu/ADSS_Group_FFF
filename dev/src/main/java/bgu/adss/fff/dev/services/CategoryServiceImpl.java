package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.CategoryRepository;
import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.CategoryException;
import bgu.adss.fff.dev.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository, ProductRepository productRepository) {
        this.categoryRepository = repository;
        this.productRepository = productRepository;
        Category superCategory = new Category("Super", 0, new LinkedList<>(), new LinkedList<>());
        repository.save(superCategory);
    }


    @Override
    public Category createCategory(Category category, String parent) {

        if(parent == null){
            throw new CategoryException("Parent cannot be null", HttpStatus.BAD_REQUEST);
        }

        if(category == null){
            throw new CategoryException("Category cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (categoryRepository.existsById(category.getCategoryName())) {
            throw new CategoryException("Category already exists", HttpStatus.BAD_REQUEST);
        }

        Category parentCategory = categoryRepository.findById(parent).orElseThrow(() -> new CategoryException("Parent not found", HttpStatus.NOT_FOUND));
        if(parentCategory.getLevel() == 3){
            throw new CategoryException("Cannot add category to a category with level 3", HttpStatus.BAD_REQUEST);
        }
        category.setLevel(parentCategory.getLevel() + 1);
        parentCategory.getChildren().add(category);

        Category addedCategory = categoryRepository.save(category);
        categoryRepository.save(parentCategory);

        return addedCategory;
    }

    @Override
    public Category getCategory(String name) {

        if(name == null) {
            throw new CategoryException("Category name cannot be null", HttpStatus.BAD_REQUEST);
        }

        return categoryRepository.findById(name).orElseThrow(() -> new CategoryException("Category not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Category> getCategories() {

        return categoryRepository.findAll();

    }

    @Override
    public Category updateCategory(String name, Category category) {

        if (category == null) {
            throw new CategoryException("Product cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (!categoryRepository.existsById(category.getCategoryName())) {
            throw new CategoryException("Product not found", HttpStatus.NOT_FOUND);
        }

        return categoryRepository.save(category);

    }

    @Override
    public void deleteCategory(String name) {

        if(!categoryRepository.existsById(name)){
            throw new CategoryException("Category not found", HttpStatus.NOT_FOUND);
        }

        categoryRepository.deleteById(name);

    }

    @Override
    public Category updateChildren(String name, List<Category> children) {

        if (!categoryRepository.existsById(name)) {
            throw new ProductException("Category not found", HttpStatus.NOT_FOUND);
        }

        Category category = categoryRepository.findById(name).orElseThrow(() -> new CategoryException("Category not found", HttpStatus.NOT_FOUND));

        // Delete current children of category
        categoryRepository.deleteAll(category.getChildren());
        category.setChildren(new LinkedList<>());

        // Fill with new sub categories
        for (Category subCategory : children) {
            category.getChildren().add(subCategory);
            categoryRepository.save(subCategory);
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category updateProducts(String name, List<Product> products) {

        if (!categoryRepository.existsById(name)) {
            throw new ProductException("Category not found", HttpStatus.NOT_FOUND);
        }

        Category category = categoryRepository.findById(name).orElseThrow(() -> new CategoryException("Category not found", HttpStatus.NOT_FOUND));

        // Delete all products from category
        category.setProducts(new LinkedList<>());

        // Fill with new products
        for (Product product : products) {
            category.getProducts().add(product);
        }

        return categoryRepository.save(category);
    }

    @Override
    public void addCategoryDiscount(String name, Discount discount) {
        Category category = categoryRepository.findById(name).orElseThrow(() -> new CategoryException("Category not found", HttpStatus.NOT_FOUND));
        for(Product product : category.getProducts()){
            product.setDiscount(discount);
            productRepository.save(product);
        }
        categoryRepository.save(category);
    }

    @Override
    public void addProduct(long id, String[] categories) {
        if(categories == null || categories.length != 3){
            throw new ProductException("Product must belong to 3 levels of categories", HttpStatus.BAD_REQUEST);
        }

        Category superCategory = categoryRepository.findById("Super").orElseThrow(() -> new CategoryException("Super category not found", HttpStatus.NOT_FOUND));
        for(String categoryName : categories){
            Category category = categoryRepository.findById(categoryName).orElseThrow(() -> new CategoryException("Category not found", HttpStatus.NOT_FOUND));
            if(!superCategory.getChildren().contains(category)){
                throw new ProductException("This category does not belong to the super category's children", HttpStatus.BAD_REQUEST);
            }
            superCategory = category;
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductException("Product not found", HttpStatus.NOT_FOUND));

        // Reverse the array to add the product to the most specific category first
        String[] reversedCategories = new String[3];
        for(int i = 0; i < 3; i++){
            reversedCategories[i] = categories[3 - i - 1];
        }

        for(String categoryName : reversedCategories){
            Category category = categoryRepository.findById(categoryName).orElseThrow(() -> new CategoryException("Category not found", HttpStatus.NOT_FOUND));
            category.getProducts().add(product);
            categoryRepository.save(category);
        }
        categoryRepository.findById("Super").ifPresent(category -> { category.getProducts().add(product); categoryRepository.save(category);});
    }

}
