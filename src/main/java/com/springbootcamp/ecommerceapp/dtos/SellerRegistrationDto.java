package com.springbootcamp.ecommerceapp.dtos;

import com.springbootcamp.ecommerceapp.validators.ValidGST;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


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



    public String getGST() {
        return GST;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

}
