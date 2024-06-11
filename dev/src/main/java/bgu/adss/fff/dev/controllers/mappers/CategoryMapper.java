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


    /**
     * Maps a category to a categoryDto
     * @param category
     * @return categoryDto
     */
    public static CategoryDto map(Category category){
        CategoryDto[] children = CategoryMapper.map(category.getChildren());
        ProductDto[] products = ProductMapper.map(category.getProducts());
        return new CategoryDto(category.getCategoryName(), category.getLevel(), children, products);
    }

    /**
     * Maps a requestCategoryDto to a category
     * @param dto requestCategoryDto that converts to category
     * @return category
     */
    public static Category map(RequestCategoryDto dto){
        return new Category(dto.categoryName(), 0, new LinkedList<>(), new LinkedList<>());
    }

    /**
     * Maps a categoryDto to a category
     * @param categoryDto categoryDto that converts to category
     * @return category
     */
    public static Category map(CategoryDto categoryDto){
        return new Category(
                categoryDto.categoryName(),
                categoryDto.level(),
                CategoryMapper.map(categoryDto.children()),
                ProductMapper.map(categoryDto.products())
        );
    }

    /**
     * Maps a list of categories to an array of categoryDtos
     * @param categories list of categories
     * @return array of categoryDtos
     */
    public static CategoryDto[] map(List<Category> categories) {
        return categories.stream().map(CategoryMapper::map).toArray(CategoryDto[]::new);
    }

    /**
     * Maps an array of categoryDtos to a list of categories
     * @param categoryDtos array of categoryDtos
     * @return list of categories
     */
    public static List<Category> map(CategoryDto[] categoryDtos) {
        return Stream.of(categoryDtos).map(CategoryMapper::map).collect(Collectors.toList());
    }

}
