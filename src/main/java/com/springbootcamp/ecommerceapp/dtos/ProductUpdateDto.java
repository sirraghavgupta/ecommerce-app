package com.springbootcamp.ecommerceapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto {

    private String name;
    private String description;
    private Boolean isReturnable = false;
    private Boolean isCancelleable = false;

}
