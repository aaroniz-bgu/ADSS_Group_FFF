package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.domain.models.Item;
import bgu.adss.fff.dev.domain.models.Product;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);
    Category getCategory(String name);
    List<Category> getCategories();
    Category updateCategory(String name, Category category);
    void deleteCategory(String name);
    Category updateChildren(String name, List<Category> children);
    Category updateProducts(String name, List<Product> products);

}
