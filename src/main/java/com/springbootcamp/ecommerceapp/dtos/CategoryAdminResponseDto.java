package com.springbootcamp.ecommerceapp.dtos;

import com.springbootcamp.ecommerceapp.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAdminResponseDto {

    CategoryDto category;
    Set<CategoryDto> subCategories;
    Set<CategoryMetadataFieldDto> fieldValues;

}
