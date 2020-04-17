package com.springbootcamp.ecommerceapp.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariationSellerDto {

    private Long id;

    @NotNull
    private Long productId;
    private Integer quantityAvailable;
    private Double price;

    private String primaryImage;
    private List<String> secondaryImages;

    @NotNull
    private Map<String, String> attributes = new LinkedHashMap<>();

    private ProductSellerDto productDto;

}
