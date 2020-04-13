package com.springbootcamp.ecommerceapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserViewProfileDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String image;
}
