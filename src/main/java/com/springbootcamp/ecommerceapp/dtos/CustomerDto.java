package com.springbootcamp.ecommerceapp.dtos;


import com.springbootcamp.ecommerceapp.entities.Customer;
import com.springbootcamp.ecommerceapp.validators.PasswordMatches;
import com.springbootcamp.ecommerceapp.validators.ValidEmail;
import com.springbootcamp.ecommerceapp.validators.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//@PasswordMatches
public class CustomerDto extends UserDto{

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    @Size(min = 10, max = 10)
    private String contact;

    public CustomerDto() {
    }

    public CustomerDto(@NotNull @NotEmpty String email, @NotNull @NotEmpty String password,
                       @NotNull @NotEmpty String confirmPassword, @NotNull @NotEmpty String name,
                       @NotNull @NotEmpty @Size(min = 10, max = 10) String contact) {
        super(email, password, confirmPassword);
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
