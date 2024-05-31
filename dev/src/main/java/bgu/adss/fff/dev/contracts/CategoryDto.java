package bgu.adss.fff.dev.contracts;

/**
 * Contract for Category details
 */

import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.domain.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Contract for Report details
 * @param categoryName
// * @param children
// * @param products
 */
public record CategoryDto(
        @JsonProperty("categoryName") String categoryName
//        @JsonProperty("children") List<Category> children,
//        @JsonProperty("products") List<Product> products
) { }
