package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.DiscountDto;
import bgu.adss.fff.dev.contracts.RequestDiscountDto;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static bgu.adss.fff.dev.controllers.mappers.DiscountMapper.map;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    private final DiscountService service;

    @Autowired
    public DiscountController(DiscountService service) {
        this.service = service;
    }

    // Basic CRUD operations

    @PostMapping
    public ResponseEntity<DiscountDto> createDiscount(@RequestBody RequestDiscountDto request) {
        Discount discount = service.createDiscount(map(request));
        DiscountDto discountDto = map(discount);
        return new ResponseEntity<>(discountDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDto> getDiscount(@PathVariable long id) {
        Discount discount = service.getDiscount(id);
        DiscountDto discountDto = map(discount);
        return ResponseEntity.ok(discountDto);
    }

    @PutMapping
    public ResponseEntity<?> updateDiscount(@RequestBody DiscountDto request) {
        service.updateDiscount(map(request));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscount(@PathVariable long id) {
        service.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }
}
