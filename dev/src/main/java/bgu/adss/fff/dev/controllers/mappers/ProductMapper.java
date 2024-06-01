package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.contracts.RequestProductDto;
import bgu.adss.fff.dev.domain.models.Product;

import java.util.LinkedList;

public class ProductMapper {

    public static ProductDto map(Product product){
        System.out.println(product.getShelves());
        System.out.println(product.getStorage());
        return new ProductDto(
                product.getProductID(),
                product.getProductName(),
                product.getPrice(),
                product.getDiscount() == null ? null : DiscountMapper.map(product.getDiscount()),
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
                productDto.discount() == null ? null : DiscountMapper.map(productDto.discount()),
                ItemMapper.map(productDto.shelves()),
                ItemMapper.map(productDto.storage()),
                productDto.minimalQuantity()
        );
    }

    public static Product map(RequestProductDto requestProductDto) {
        return new Product(
                requestProductDto.productID(),
                requestProductDto.productName(),
                requestProductDto.price(),
                null,
                new LinkedList<>(),
                new LinkedList<>(),
                requestProductDto.minimalQuantity()
        );
    }

}
