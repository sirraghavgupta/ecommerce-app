package com.springbootcamp.ecommerceapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFilteringDetailsDto {

    private Map<String, Set<String>> fieldValues;
    private Set<String> brands;
    private Double minPrice;
    private Double maxPrice;

}
