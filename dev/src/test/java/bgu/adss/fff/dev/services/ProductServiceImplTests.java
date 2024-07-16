package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.contracts.RequestDiscountDto;
import bgu.adss.fff.dev.contracts.RequestItemDto;
import bgu.adss.fff.dev.contracts.RequestProductDto;
import bgu.adss.fff.dev.controllers.mappers.DiscountMapper;
import bgu.adss.fff.dev.controllers.mappers.ItemMapper;
import bgu.adss.fff.dev.controllers.mappers.ProductMapper;
import bgu.adss.fff.dev.data.ProductRepository;
import bgu.adss.fff.dev.domain.models.Branch;
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
import static org.junit.jupiter.api.Assertions.*;
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
        product = map(new RequestProductDto(123, "Milk", 10.0f, 5, 1, 3.0f));
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
        RequestItemDto requestItemDto = new RequestItemDto("31-07-2024", false, 10, "Main");
        List<Item> items = ItemMapper.map(requestItemDto);

        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        int beforeAmount = productService.getProduct(product.getProductID()).getQuantity();
        productService.addItems(product.getProductID(), items);
        int afterAmount = productService.getProduct(product.getProductID()).getQuantity();
        assertEquals(beforeAmount + 10, afterAmount);
    }

    @Test
    void testUpdateMoveItems() {
        testUpdateAddItems();

        int beforeStorage = productService.getProduct(product.getProductID()).getStorage().size();
        int beforeShelves = productService.getProduct(product.getProductID()).getShelves().size();
        productService.moveToShelves(product.getProductID(), 5);
        int afterStorage = productService.getProduct(product.getProductID()).getStorage().size();
        int afterShelves = productService.getProduct(product.getProductID()).getShelves().size();
        assertEquals(beforeStorage - 5, afterStorage);
        assertEquals(beforeShelves + 5, afterShelves);
    }

    @Test
    void testAddDiscount() {
        when(productRepository.save(product)).thenReturn(product);
        assertEquals(product, productService.createProduct(product));
        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));

        Discount discount = DiscountMapper.map(
                new RequestDiscountDto("01-07-2024", "31-07-2024", 0.1f));
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

    @Test
    void testUpdateSupplier() {
        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        productService.updateSupplier(product.getProductID(), 2, 2.5f);
        assertEquals(productRepository.findById(product.getProductID()).orElseThrow().getSupplierID(), 2);
        assertEquals(productRepository.findById(product.getProductID()).orElseThrow().getSupplierPrice(), 2.5f);
    }

    @Test
    void testUpdateSupplierPriceNotLowEnough() {
        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        assertThrows(ProductException.class, () -> productService.updateSupplier(product.getProductID(), 2, 5.0f));
    }

    @Test
    void testSellItems() {
        testUpdateAddItems();

        int beforeAmount = productService.getProduct(product.getProductID()).getQuantity();
        productService.sellItems(product.getProductID(), 5, new Branch("Main"));
        int afterAmount = productService.getProduct(product.getProductID()).getQuantity();
        assertEquals(beforeAmount - 5, afterAmount);
    }

    @Test
    void testSellItemsMoreThanCanSell() {
        testUpdateAddItems();

        assertThrows(ProductException.class, () -> productService.sellItems(product.getProductID(), 100, new Branch("Main")));
    }

    @Test
    void testThrowStorageItem() {
        testUpdateAddItems();

        long itemID = productService.getProduct(product.getProductID()).getStorage().get(0).getItemID();
        System.out.println(itemID);

        int beforeAmount = productService.getProduct(product.getProductID()).getQuantity();
        productService.setItemDefective(product.getProductID(), itemID, true);
        productService.throwItem(product.getProductID(), itemID, new Branch("Main"));
        int afterAmount = productService.getProduct(product.getProductID()).getQuantity();
        assertEquals(beforeAmount - 1, afterAmount);
    }

    @Test
    void testThrowShelfItem() {
        testUpdateAddItems();

        long itemID = productService.getProduct(product.getProductID()).getShelves().get(0).getItemID();

        int beforeAmount = productService.getProduct(product.getProductID()).getQuantity();
        productService.setItemDefective(product.getProductID(), itemID, true);
        productService.throwItem(product.getProductID(), itemID, new Branch("Main"));
        int afterAmount = productService.getProduct(product.getProductID()).getQuantity();
        assertEquals(beforeAmount - 1, afterAmount);
    }

    @Test
    void testThrowItemNotDefective() {
        testUpdateAddItems();

        long itemID = productService.getProduct(product.getProductID()).getStorage().get(0).getItemID();

        assertThrows(ProductException.class, () -> productService.throwItem(product.getProductID(), itemID, new Branch("Main")));
    }

    @Test
    void testThrowItemNotFound() {
        testUpdateAddItems();

        long fakeItemID = 100;
        assertThrows(ProductException.class, () -> productService.throwItem(product.getProductID(), fakeItemID, new Branch("Main")));
    }

    @Test
    void testOrderItems() {
        testCreateSuccess();

        when(productRepository.existsById(product.getProductID())).thenReturn(true);
        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        productService.orderItems(product.getProductID(), new Branch("Main"));
        int afterAmount = productService.getProduct(product.getProductID()).getQuantity();
        assertTrue(afterAmount >= product.getMinimalQuantity());
    }

}
