package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.domain.models.Category;

import java.util.Locale;

public class CategoryMapper {

    public static CategoryDto map(Category category){
        return new CategoryDto(category.getCategoryName());
    }

}
