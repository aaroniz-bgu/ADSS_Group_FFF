package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.contracts.RequestCategoryDto;
import bgu.adss.fff.dev.data.CategoryRepository;
import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.exceptions.CategoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.Optional;

import static bgu.adss.fff.dev.controllers.mappers.CategoryMapper.map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CategoryServiceImplTests {

    @Autowired
    private CategoryServiceImpl categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    private Category superCategory, category;

    @BeforeEach
    void before() {
        superCategory = new Category("Super", 0, new LinkedList<>(), new LinkedList<>());
        when(categoryRepository.findById("Super")).thenReturn(Optional.of(superCategory));

        category = map(new RequestCategoryDto("Food"));
    }

    @Test
    void testCreateSuccess() {
        when(categoryRepository.save(category)).thenReturn(category);
        assertEquals(category, categoryService.createCategory(category, "Super"));
    }

    @Test
    void testCreateAlreadyExists() {
        when(categoryRepository.existsById(category.getCategoryName())).thenReturn(true);
        assertThrows(CategoryException.class, () -> categoryService.createCategory(category, "Super"));
    }

    @Test
    void testUpdateSuccess() {
        when(categoryRepository.existsById(category.getCategoryName())).thenReturn(true);
        when(categoryRepository.save(category)).thenReturn(category);
        assertEquals(category, categoryService.updateCategory(category.getCategoryName(), category));
    }

    @Test
    void testUpdateNotFound() {
        when(categoryRepository.findById(category.getCategoryName())).thenReturn(Optional.empty());
        assertThrows(CategoryException.class, () -> categoryService.updateCategory(category.getCategoryName(), category));
    }

    @Test
    void testGetSuccess() {
        when(categoryRepository.findById(category.getCategoryName())).thenReturn(Optional.of(category));
        assertEquals(category, categoryService.getCategory(category.getCategoryName()));
    }

    @Test
    void testGetNotFound() {
        when(categoryRepository.findById(category.getCategoryName())).thenReturn(Optional.empty());
        assertThrows(CategoryException.class, () -> categoryService.getCategory(category.getCategoryName()));
    }

    @Test
    void testDeleteSuccess() {
        when(categoryRepository.existsById(category.getCategoryName())).thenReturn(true);
        categoryService.deleteCategory(category.getCategoryName());
    }

    @Test
    void testDeleteNotFound() {
        when(categoryRepository.findById(category.getCategoryName())).thenReturn(Optional.empty());
        assertThrows(CategoryException.class, () -> categoryService.deleteCategory(category.getCategoryName()));
    }

}
