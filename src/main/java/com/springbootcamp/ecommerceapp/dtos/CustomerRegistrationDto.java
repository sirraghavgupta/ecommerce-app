package com.springbootcamp.ecommerceapp.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CustomerRegistrationDto extends UserRegistrationDto {

    @NotNull
    @NotEmpty
    @Size(min = 10, max = 10)
    private String contact;
}
