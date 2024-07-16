package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.contracts.RequestDiscountDto;
import bgu.adss.fff.dev.data.DiscountRepository;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.exceptions.DiscountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static bgu.adss.fff.dev.controllers.mappers.DiscountMapper.map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DiscountServiceImplTests {

    @Autowired
    private DiscountServiceImpl discountService;

    @MockBean
    private DiscountRepository discountRepository;

    private Discount discount;

    @BeforeEach
    void before() {
        discount = new Discount(1, LocalDate.parse("2024-07-01"), LocalDate.parse("2024-07-31"),
                0.1f);
    }

    @Test
    void testCreateSuccess() {
        when(discountRepository.save(discount)).thenReturn(discount);
        assertEquals(discount, discountService.createDiscount(discount));
    }

    @Test
    void testCreateAlreadyExists() {
        when(discountRepository.existsById(discount.getDiscountID())).thenReturn(true);
        assertThrows(DiscountException.class, () -> discountService.createDiscount(discount));
    }

    @Test
    void testUpdateSuccess() {
        when(discountRepository.existsById(discount.getDiscountID())).thenReturn(true);
        when(discountRepository.save(discount)).thenReturn(discount);
        assertEquals(discount, discountService.updateDiscount(discount));
    }

    @Test
    void testUpdateNotFound() {
        when(discountRepository.findById(discount.getDiscountID())).thenReturn(Optional.empty());
        assertThrows(DiscountException.class, () -> discountService.updateDiscount(discount));
    }

    @Test
    void testGetSuccess() {
        when(discountRepository.findById(discount.getDiscountID())).thenReturn(Optional.of(discount));
        assertEquals(discount, discountService.getDiscount(discount.getDiscountID()));
    }

    @Test
    void testGetNotFound() {
        when(discountRepository.findById(discount.getDiscountID())).thenReturn(Optional.empty());
        assertThrows(DiscountException.class, () -> discountService.getDiscount(discount.getDiscountID()));
    }

    @Test
    void testDeleteSuccess() {
        when(discountRepository.existsById(discount.getDiscountID())).thenReturn(true);
        discountService.deleteDiscount(discount.getDiscountID());
    }

    @Test
    void testDeleteNotFound() {
        when(discountRepository.findById(discount.getDiscountID())).thenReturn(Optional.empty());
        assertThrows(DiscountException.class, () -> discountService.deleteDiscount(discount.getDiscountID()));
    }

}
