package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.contracts.RequestCategoryDto;
import bgu.adss.fff.dev.domain.models.Category;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryMapper {



    public static CategoryDto map(Category category){
        CategoryDto[] children = CategoryMapper.map(category.getChildren());
        ProductDto[] products = ProductMapper.map(category.getProducts());
        return new CategoryDto(category.getCategoryName(), category.getLevel(), children, products);
    }

    public static Category map(RequestCategoryDto dto){
        return new Category(dto.categoryName(), 0, new LinkedList<>(), new LinkedList<>());
    }

    public static Category map(CategoryDto categoryDto){
        return new Category(
                categoryDto.categoryName(),
                categoryDto.level(),
                CategoryMapper.map(categoryDto.children()),
                ProductMapper.map(categoryDto.products())
        );
    }

    public static CategoryDto[] map(List<Category> categories) {
        return categories.stream().map(CategoryMapper::map).toArray(CategoryDto[]::new);
    }

    public static List<Category> map(CategoryDto[] categoryDtos) {
        return Stream.of(categoryDtos).map(CategoryMapper::map).collect(Collectors.toList());
    }

}
