package com.springbootcamp.ecommerceapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCustomerViewDto{

    private ProductSellerDto productDto;
    private Set<ProductVariationSellerDto> variations;
}
