package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.CategoryRepository;
import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.CategoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }


    @Override
    public Category createCategory(Category category) {

        if(category == null){
            throw new CategoryException("Category cannot be null");
        }

        if (repository.existsById(category.getCategoryName())) {
            throw new CategoryException("Category already exists");
        }

        // TODO: Any other business logic

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
    public List<Category> getCategory() {

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

        //TODO: Any other business logic

        return repository.save(category);

    }

    @Override
    public void deleteCategory(String name) {

        if(!repository.existsById(name)){
            throw new CategoryException("Category not found");
        }

        repository.deleteById(name);

    }

}
