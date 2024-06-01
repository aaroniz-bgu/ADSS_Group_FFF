package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.domain.models.Product;

public class ProductMapper {

    public static ProductDto map(Product product){
        return new ProductDto(
                product.getProductID(),
                product.getProductName(),
                product.getPrice(),
                DiscountMapper.map(product.getDiscount()),
                ItemMapper.map(product.getShelves()),
                ItemMapper.map(product.getStorage()),
                product.getMinimalQuantity()
        );
    }

}
