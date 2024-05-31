package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);
    Category getCategory(String name);
    List<Category> getCategory();
    Category updateCategory(String name, Category category);
    void deleteCategory(String name);

}
