package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.CategoryRepository;
import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.domain.models.Item;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.CategoryException;
import bgu.adss.fff.dev.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw new CategoryException("Parent cannot be null");
        }

        if(category == null){
            throw new CategoryException("Category cannot be null");
        }

        if (categoryRepository.existsById(category.getCategoryName())) {
            throw new CategoryException("Category already exists");
        }

        Category parentCategory = categoryRepository.findById(parent).orElseThrow(() -> new CategoryException("Parent not found"));
        if(parentCategory.getLevel() == 3){
            throw new CategoryException("Cannot add category to a category with level 3");
        }
        category.setLevel(parentCategory.getLevel() + 1);
        parentCategory.getChildren().add(category);
        categoryRepository.save(parentCategory);
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategory(String name) {

        if(name == null) {
            throw new CategoryException("Category name cannot be null");
        }

        return categoryRepository.findById(name).orElseThrow(() -> new CategoryException("Category not found"));
    }

    @Override
    public List<Category> getCategories() {

        return categoryRepository.findAll();

    }

    @Override
    public Category updateCategory(String name, Category category) {

        if (category == null) {
            throw new CategoryException("Product cannot be null");
        }

        if (!categoryRepository.existsById(category.getCategoryName())) {
            throw new CategoryException("Product not found");
        }

        return categoryRepository.save(category);

    }

    @Override
    public void deleteCategory(String name) {

        if(!categoryRepository.existsById(name)){
            throw new CategoryException("Category not found");
        }

        categoryRepository.deleteById(name);

    }

    @Override
    public Category updateChildren(String name, List<Category> children) {

        if (!categoryRepository.existsById(name)) {
            throw new ProductException("Category not found");
        }

        Category category = categoryRepository.findById(name).orElseThrow(() -> new CategoryException("Category not found"));

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
            throw new ProductException("Category not found");
        }

        Category category = categoryRepository.findById(name).orElseThrow(() -> new CategoryException("Category not found"));

        // Delete all products from category
        category.setProducts(new LinkedList<>());

        // Fill with new products
        for (Product product : products) {
            category.getProducts().add(product);
        }

        return categoryRepository.save(category);
    }

    public void addCategoryDiscount(String name, Discount discount) {
        Category category = categoryRepository.findById(name).orElseThrow(() -> new CategoryException("Category not found"));
        for(Product product : category.getProducts()){
            product.setDiscount(discount);
            productRepository.save(product);
        }
        categoryRepository.save(category);
    }

}
