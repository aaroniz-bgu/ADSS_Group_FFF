package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.contracts.RequestProductDto;
import bgu.adss.fff.dev.domain.models.Product;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductMapper {

    /**
     * Maps Product to ProductDto
     * @param product Product to be mapped to ProductDto
     * @return productDto
     */
    public static ProductDto map(Product product){
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

    /**
     * Maps ProductDto to Product
     * @param productDto ProductDto to be mapped to Product
     * @return product
     */
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

    /**
     * Maps RequestProductDto to Product
     * @param requestProductDto RequestProductDto to be mapped to Product
     * @return product
     */
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

    /**
     * Maps an array of products to an array of productDtos
     * @param products array of products to be mapped to array of productDtos
     * @return array of productDtos
     */
    public static ProductDto[] map(List<Product> products) {
        return products.stream().map(ProductMapper::map).toArray(ProductDto[]::new);
    }

    /**
     * Maps an array of productDtos to an array of products
     * @param productDTOs array of productDtos to be mapped to array of products
     * @return array of products
     */
    public static List<Product> map(ProductDto[] productDTOs) {
        return Stream.of(productDTOs).map(ProductMapper::map).collect(Collectors.toList());
    }

}
