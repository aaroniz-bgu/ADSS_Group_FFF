package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.ProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceImplTests {

    @Autowired
    private ProductServiceImpl productService;

    @MockBean
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void before() {
        product = new Product(1, "product", 10.0f, null, new LinkedList<>(), new LinkedList<>(), 5);
    }

    @Test
    void testCreate() {
        when(productRepository.save(product)).thenReturn(product);
        assertEquals(product, productService.createProduct(product));
    }

    @Test
    void testGet() {
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));
        assertEquals(product, productService.getProduct(product.getProductID()));
        when(productRepository.findById(-1L)).thenReturn(Optional.empty());
        assertThrows(ProductException.class, () -> productService.getProduct(-1L));
    }

}
