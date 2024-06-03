package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.data.DiscountRepository;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.exceptions.DiscountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    private Discount save(Discount discount) {
        return discountRepository.save(discount);
    }

    private boolean doesDiscountExist(long id) {
        return discountRepository.existsById(id);
    }

    public long generateRandomDiscountID() {
        long id =  new Random().nextLong();

        while (id == 0 || doesDiscountExist(id)) {
            id = new Random().nextLong();
        }

        return id;
    }

    @Override
    public Discount createDiscount(Discount discount) {

        if (discount == null) {
            throw new DiscountException("Discount cannot be null");
        }

        discount.setDiscountID(generateRandomDiscountID());

        return save(discount);
    }

    @Override
    public Discount getDiscount(long id) {
        return null;
    }

    @Override
    public Discount updateDiscount(Discount discount) {
        return null;
    }

    @Override
    public void deleteDiscount(long id) {

    }
}
