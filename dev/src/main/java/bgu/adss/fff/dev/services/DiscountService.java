package bgu.adss.fff.dev.services;

import bgu.adss.fff.dev.domain.models.Discount;

public interface DiscountService {

    Discount createDiscount(Discount discount);
    Discount getDiscount(long id);
    Discount updateDiscount(Discount discount);
    void deleteDiscount(long id);

}
