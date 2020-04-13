package com.springbootcamp.ecommerceapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private String addressLine;
    private String city;
    private String state;

    @Size(min = 6, max = 6, message = "Zipcode should be of length 6")
    private String zipCode;
    private String country;
    private String label;

}
