package com.springbootcamp.ecommerceapp.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductSellerDto {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String brand;

    @NotNull
    private Long categoryId;

    private CategoryDto categoryDto;
    private String description;
    private Boolean isReturnable = false;
    private Boolean isCancelleable = false;

}
