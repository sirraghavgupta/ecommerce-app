package com.springbootcamp.ecommerceapp.dtos;

import com.springbootcamp.ecommerceapp.validators.ValidGST;
import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerViewProfileDto extends UserViewProfileDto{

    @ValidGST
    @Size(min = 15, max = 15)
    private String GST;

    private String companyName;

    @Size(min = 10, max = 10)
    private String companyContact;

    private AddressDto addressDto;

}
