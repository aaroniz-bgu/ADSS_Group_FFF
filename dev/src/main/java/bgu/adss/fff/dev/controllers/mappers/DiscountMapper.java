package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.DiscountDto;
import bgu.adss.fff.dev.contracts.RequestDiscountDto;
import bgu.adss.fff.dev.domain.models.Discount;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DiscountMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static DiscountDto map(Discount discount){
        return new DiscountDto(
                discount.getDiscountID(),
                discount.getStartDate().format(formatter),
                discount.getEndDate().format(formatter),
                discount.getDiscountPercent()
        );
    }

    public static Discount map(DiscountDto discountDto){
        return new Discount(
                discountDto.discountID(),
                LocalDate.parse(discountDto.startDate(), formatter),
                LocalDate.parse(discountDto.endDate(), formatter),
                discountDto.discountPercent()
        );
    }

    public static Discount map(RequestDiscountDto requestDiscountDto){
        return new Discount(
                0,
                LocalDate.parse(requestDiscountDto.startDate(), formatter),
                LocalDate.parse(requestDiscountDto.endDate(), formatter),
                requestDiscountDto.discountPercent()
        );
    }

}
