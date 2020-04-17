package com.springbootcamp.ecommerceapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariationUpdateDto {

    private Integer quantityAvailable;
    private Double price;
    private Boolean isActive;

    @NotNull
    private Map<String, String> attributes = new LinkedHashMap<>();

}
