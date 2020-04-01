package com.springbootcamp.ecommerceapp.dtos;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class CustomerRegistrationDto extends UserRegistrationDto {

    @NotNull
    @NotEmpty
    @Size(min = 10, max = 10)
    private String contact;


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
