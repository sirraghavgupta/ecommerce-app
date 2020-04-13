package com.springbootcamp.ecommerceapp.dtos;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerViewProfileDto extends UserViewProfileDto{

    @Size(min = 10, max = 10, message = "Contact should be of length 10.")
    private String contact;

}
