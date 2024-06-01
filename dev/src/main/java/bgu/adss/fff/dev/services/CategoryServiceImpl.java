package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.CategoryRepository;
import bgu.adss.fff.dev.domain.models.Category;
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
        // TODO: Check if category already exists
        // TODO: Any other business logic
        return repository.save(category);
    }

    @Override
    public Category getCategory(String name) {
        return repository.findById(name).orElseThrow(CategoryException::new);
    }

    @Override
    public List<Category> getCategory() {
        return null;
    }

    @Override
    public Category updateCategory(String name, Category category) {
        return null;
    }

    @Override
    public void deleteCategory(String name) {

    }

}
