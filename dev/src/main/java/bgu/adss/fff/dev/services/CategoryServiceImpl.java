package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.CategoryRepository;
import bgu.adss.fff.dev.domain.models.Category;
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

    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
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

        if (repository.existsById(category.getCategoryName())) {
            throw new CategoryException("Category already exists");
        }

        Category parentCategory = repository.findById(parent).orElseThrow(() -> new CategoryException("Parent not found"));
        if(parentCategory.getLevel() == 3){
            throw new CategoryException("Cannot add category to a category with level 3");
        }
        category.setLevel(parentCategory.getLevel() + 1);
        parentCategory.getChildren().add(category);
        repository.save(parentCategory);
        return repository.save(category);
    }

    @Override
    public Category getCategory(String name) {

        if(name == null) {
            throw new CategoryException("Category name cannot be null");
        }

        return repository.findById(name).orElseThrow(() -> new CategoryException("Category not found"));
    }

    @Override
    public List<Category> getCategories() {

        return repository.findAll();

    }

    @Override
    public Category updateCategory(String name, Category category) {

        if (category == null) {
            throw new CategoryException("Product cannot be null");
        }

        if (!repository.existsById(category.getCategoryName())) {
            throw new CategoryException("Product not found");
        }

        return repository.save(category);

    }

    @Override
    public void deleteCategory(String name) {

        if(!repository.existsById(name)){
            throw new CategoryException("Category not found");
        }

        repository.deleteById(name);

    }

    @Override
    public Category updateChildren(String name, List<Category> children) {

        if (!repository.existsById(name)) {
            throw new ProductException("Category not found");
        }

        Category category = repository.findById(name).orElseThrow(() -> new CategoryException("Category not found"));

        // Delete current children of category
        repository.deleteAll(category.getChildren());
        category.setChildren(new LinkedList<>());

        // Fill with new sub categories
        for (Category subCategory : children) {
            category.getChildren().add(subCategory);
            repository.save(subCategory);
        }

        return repository.save(category);
    }

    @Override
    public Category updateProducts(String name, List<Product> products) {

        if (!repository.existsById(name)) {
            throw new ProductException("Category not found");
        }

        Category category = repository.findById(name).orElseThrow(() -> new CategoryException("Category not found"));

        // Delete all products from category
        category.setProducts(new LinkedList<>());

        // Fill with new products
        for (Product product : products) {
            category.getProducts().add(product);
        }

        return repository.save(category);
    }

}
