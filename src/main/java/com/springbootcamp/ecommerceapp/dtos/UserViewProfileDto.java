package com.springbootcamp.ecommerceapp.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserViewProfileDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String image;
}
