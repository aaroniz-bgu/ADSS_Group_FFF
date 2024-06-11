package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.DiscountRepository;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.exceptions.DiscountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    private boolean doesDiscountExist(long id) {
        return discountRepository.existsById(id);
    }

    private long generateRandomDiscountID() {
        long id =  new Random().nextLong();

        while (id == 0 || doesDiscountExist(id)) {
            id = new Random().nextLong();
        }

        return id;
    }

    @Override
    public Discount createDiscount(Discount discount) {

        if (discount == null) {
            throw new DiscountException("Discount cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (discount.getDiscountID() == 0)
            discount.setDiscountID(generateRandomDiscountID());

        if (doesDiscountExist(discount.getDiscountID())) {
            throw new DiscountException("Discount already exists", HttpStatus.BAD_REQUEST);
        }

        return discountRepository.save(discount);
    }

    @Override
    public Discount getDiscount(long id) {
        return discountRepository.findById(id).orElseThrow(() -> new DiscountException("Discount not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Discount updateDiscount(Discount discount) {

        if (discount == null) {
            throw new DiscountException("Discount cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (!doesDiscountExist(discount.getDiscountID())) {
            throw new DiscountException("Discount not found", HttpStatus.NOT_FOUND);
        }

        return discountRepository.save(discount);
    }

    @Override
    public void deleteDiscount(long id) {

        if (!doesDiscountExist(id)) {
            throw new DiscountException("Discount not found", HttpStatus.NOT_FOUND);
        }

        discountRepository.deleteById(id);
    }
}
