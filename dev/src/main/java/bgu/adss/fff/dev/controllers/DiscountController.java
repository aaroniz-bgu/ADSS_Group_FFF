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

    /**
     * Establish a new discount in the system
     * @param request The request containing the following data:
     *                - {@code float discountPercentage}: The percentage of the discount
     *                - {@code String startDate}: The start date of the discount
     *                - {@code String endDate}: The end date of the discount
     * @return ResponseEntity containing the created discount if successful, or a bad request if the input is invalid
     */
    @PostMapping
    public ResponseEntity<DiscountDto> createDiscount(@RequestBody RequestDiscountDto request) {
        Discount discount = service.createDiscount(map(request));
        DiscountDto discountDto = map(discount);
        return new ResponseEntity<>(discountDto, HttpStatus.CREATED);
    }

    /**
     * Fetches a discount by its id
     * @param id The id of the discount
     * @return ResponseEntity containing the discount if found, or no content if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<DiscountDto> getDiscount(@PathVariable long id) {
        Discount discount = service.getDiscount(id);
        DiscountDto discountDto = map(discount);
        return ResponseEntity.ok(discountDto);
    }

    /**
     * Updates a discount in the system
     * @param request The request containing the following data:
     *             - {@code long discountId}: The id of the discount
     *             - {@code float discountPercentage}: The percentage of the discount
     *             - {@code String startDate}: The start date of the discount
     *             - {@code String endDate}: The end date of the discount
     * @return ResponseEntity containing the updated category if successful, or a bad request if the input is invalid
     */
    @PutMapping
    public ResponseEntity<?> updateDiscount(@RequestBody DiscountDto request) {
        service.updateDiscount(map(request));
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a discount associated with the given name from the system
     * @param id The id of the discount
     * @return ResponseEntity containing no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscount(@PathVariable long id) {
        service.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }
}
