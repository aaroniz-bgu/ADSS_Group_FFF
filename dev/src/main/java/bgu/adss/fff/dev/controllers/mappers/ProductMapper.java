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

    public static Product map(ProductDto productDto){
        return new Product(
                productDto.productID(),
                productDto.productName(),
                productDto.price(),
                DiscountMapper.map(productDto.discount()),
                ItemMapper.map(productDto.shelves(), 0),
                ItemMapper.map(productDto.storage(), 0),
                productDto.minimalQuantity()
        );
    }

}
