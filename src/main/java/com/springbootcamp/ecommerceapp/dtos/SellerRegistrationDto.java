package com.springbootcamp.ecommerceapp.dtos;

import com.springbootcamp.ecommerceapp.validators.ValidGST;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Getter
@Setter
public class SellerRegistrationDto extends UserRegistrationDto {

    @NotNull
    @NotEmpty
    @Size(min = 15, max = 15)
    @ValidGST
    private String GST;

    @NotNull
    @NotEmpty
    private String companyName;

    @NotNull
    @NotEmpty
    @Size(min = 10, max = 10)
    private String companyContact;

    @NotNull
    AddressDto addressDto;

}
