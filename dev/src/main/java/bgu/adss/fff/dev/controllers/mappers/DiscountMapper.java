package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.contracts.DiscountDto;
import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.domain.models.Discount;

import java.time.format.DateTimeFormatter;

public class DiscountMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

    public static DiscountDto map(Discount discount){
        return new DiscountDto(
                discount.getDiscountID(),
                discount.getStartDate().format(formatter),
                discount.getNumOfDays(),
                discount.getDiscountPercent(),
                discount.isValid()
        );
    }

}
