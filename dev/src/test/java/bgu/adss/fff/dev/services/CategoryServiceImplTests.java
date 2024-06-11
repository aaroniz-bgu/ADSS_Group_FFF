package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.contracts.RequestCategoryDto;
import bgu.adss.fff.dev.contracts.RequestDiscountDto;
import bgu.adss.fff.dev.controllers.mappers.DiscountMapper;
import bgu.adss.fff.dev.data.CategoryRepository;
import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.CategoryException;
import bgu.adss.fff.dev.exceptions.ProductException;
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

    @Autowired
    private ProductServiceImpl productService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ProductRepository productRepository;

    private Category superCategory, category, category2, category3;

    @BeforeEach
    void before() {
        superCategory = new Category("Super", 0, new LinkedList<>(), new LinkedList<>());
        when(categoryRepository.findById("Super")).thenReturn(Optional.of(superCategory));

        category = map(new RequestCategoryDto("Milk"));
        category2 = map(new RequestCategoryDto("Cheese"));
        category3 = map(new RequestCategoryDto("Goat Cheese"));
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
        assertThrows(CategoryException.class,
                () -> categoryService.updateCategory(category.getCategoryName(), category));
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

    @Test
    void testAddDiscount() {
        when(categoryRepository.save(category)).thenReturn(category);
        assertEquals(category, categoryService.createCategory(category, "Super"));
        when(categoryRepository.existsById(category.getCategoryName())).thenReturn(true);
        when(categoryRepository.findById(category.getCategoryName())).thenReturn(Optional.of(category));

        Discount discount = DiscountMapper.map(
                new RequestDiscountDto("01-07-2024", "31-07-2024", 0.1f)
        );
        categoryService.addCategoryDiscount(category.getCategoryName(), discount);

        when(categoryRepository.existsById(category.getCategoryName())).thenReturn(true);
        when(categoryRepository.findById(category.getCategoryName())).thenReturn(Optional.of(category));

        for (Product product : categoryService.getCategory(category.getCategoryName()).getProducts())
            assertEquals(discount, product.getDiscount());
    }

    @Test
    void testAddProduct() {
        when(categoryRepository.save(category)).thenReturn(category);
        assertEquals(category, categoryService.createCategory(category, "Super"));
        when(categoryRepository.existsById(category.getCategoryName())).thenReturn(true);
        when(categoryRepository.findById(category.getCategoryName())).thenReturn(Optional.of(category));

        when(categoryRepository.save(category2)).thenReturn(category2);
        assertEquals(category2, categoryService.createCategory(category2, category.getCategoryName()));
        when(categoryRepository.existsById(category2.getCategoryName())).thenReturn(true);
        when(categoryRepository.findById(category2.getCategoryName())).thenReturn(Optional.of(category2));

        when(categoryRepository.save(category3)).thenReturn(category3);
        assertEquals(category3, categoryService.createCategory(category3, category2.getCategoryName()));
        when(categoryRepository.existsById(category3.getCategoryName())).thenReturn(true);
        when(categoryRepository.findById(category3.getCategoryName())).thenReturn(Optional.of(category3));

        Product product = new Product(1L, "Goat Cheese 5% Fat", 2.5f, null,
                new LinkedList<>(), new LinkedList<>(), 5);
        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.existsById(product.getProductID())).thenReturn(false);
        assertEquals(product, productService.createProduct(product));
        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));

        categoryService.addProduct(
                product.getProductID(),
                new String[] {category.getCategoryName(), category2.getCategoryName(), category3.getCategoryName()}
        );

        when(categoryRepository.existsById(category.getCategoryName())).thenReturn(true);
        when(categoryRepository.findById(category.getCategoryName())).thenReturn(Optional.of(category));

        assertEquals(product, categoryService.getCategory(category.getCategoryName()).getProducts().get(0));
    }

    @Test
    void testAddProductOnly2Categories() {
        when(categoryRepository.save(category)).thenReturn(category);
        assertEquals(category, categoryService.createCategory(category, "Super"));
        when(categoryRepository.existsById(category.getCategoryName())).thenReturn(true);
        when(categoryRepository.findById(category.getCategoryName())).thenReturn(Optional.of(category));

        when(categoryRepository.save(category2)).thenReturn(category2);
        assertEquals(category2, categoryService.createCategory(category2, category.getCategoryName()));
        when(categoryRepository.existsById(category2.getCategoryName())).thenReturn(true);
        when(categoryRepository.findById(category2.getCategoryName())).thenReturn(Optional.of(category2));

        Product product = new Product(1L, "Goat Cheese 5% Fat", 2.5f,
                null, new LinkedList<>(), new LinkedList<>(), 5);
        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.existsById(product.getProductID())).thenReturn(false);
        assertEquals(product, productService.createProduct(product));
        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));

        assertThrows(
                ProductException.class,
                () -> categoryService.addProduct(
                        product.getProductID(),
                        new String[] {category.getCategoryName(), category2.getCategoryName()}
                ));
    }

}
