package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.contracts.RequestDiscountDto;
import bgu.adss.fff.dev.contracts.RequestItemDto;
import bgu.adss.fff.dev.contracts.RequestProductDto;
import bgu.adss.fff.dev.controllers.mappers.DiscountMapper;
import bgu.adss.fff.dev.controllers.mappers.ItemMapper;
import bgu.adss.fff.dev.controllers.mappers.ProductMapper;
import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.domain.models.Item;
import bgu.adss.fff.dev.domain.models.Product;
import bgu.adss.fff.dev.exceptions.ProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static bgu.adss.fff.dev.controllers.mappers.ProductMapper.map;
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
        product = map(new RequestProductDto(123, "Milk", 10.0f, 5));
    }

    @Test
    void testCreateSuccess() {
        when(productRepository.save(product)).thenReturn(product);
        assertEquals(product, productService.createProduct(product));
    }

    @Test
    void testCreateAlreadyExists() {
        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        assertThrows(ProductException.class, () -> productService.createProduct(product));
    }

    @Test
    void testUpdateSuccess() {
        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        when(productRepository.save(product)).thenReturn(product);
        assertEquals(product, productService.updateProduct(product));
    }

    @Test
    void testUpdateAddItems() {
        RequestItemDto requestItemDto = new RequestItemDto("31-07-2024", false, 10);
        List<Item> items = ItemMapper.map(requestItemDto);

        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        productService.addItems(product.getProductID(), items);
        assertEquals(10, productService.getProduct(product.getProductID()).getStorage().size());
    }

    @Test
    void testUpdateMoveItems() {
        testUpdateAddItems();

        productService.moveToShelves(product.getProductID(), 5);
        assertEquals(5, productService.getProduct(product.getProductID()).getStorage().size());
        assertEquals(5, productService.getProduct(product.getProductID()).getShelves().size());
    }

    @Test
    void testAddDiscount() {
        when(productRepository.save(product)).thenReturn(product);
        assertEquals(product, productService.createProduct(product));
        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));

        Discount discount = DiscountMapper.map(new RequestDiscountDto("01-07-2024", "31-07-2024", 0.1f));
        productService.addDiscount(product.getProductID(), discount);

        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));

        assertEquals(discount, productService.getProduct(product.getProductID()).getDiscount());
    }

    @Test
    void testUpdateNotFound() {
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.empty());
        assertThrows(ProductException.class, () -> productService.updateProduct(product));
    }

    @Test
    void testGetSuccess() {
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));
        assertEquals(product, productService.getProduct(product.getProductID()));
    }

    @Test
    void testGetNotFound() {
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.empty());
        assertThrows(ProductException.class, () -> productService.getProduct(product.getProductID()));
    }

    @Test
    void testDeleteSuccess() {
        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        productService.deleteProduct(product.getProductID());
    }

    @Test
    void testDeleteNotFound() {
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.empty());
        assertThrows(ProductException.class, () -> productService.deleteProduct(product.getProductID()));
    }

}
