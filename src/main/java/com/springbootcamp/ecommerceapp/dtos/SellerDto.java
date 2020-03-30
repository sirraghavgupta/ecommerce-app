package com.springbootcamp.ecommerceapp.dtos;

import com.springbootcamp.ecommerceapp.validators.PasswordMatches;
import com.springbootcamp.ecommerceapp.validators.ValidEmail;
import com.springbootcamp.ecommerceapp.validators.ValidGST;
import com.springbootcamp.ecommerceapp.validators.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//@PasswordMatches
public class SellerDto extends UserDto{


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


    public SellerDto() {
    }

    public SellerDto(@NotNull @NotEmpty String email, @NotNull @NotEmpty String password,
                     @NotNull @NotEmpty String confirmPassword, @NotNull @NotEmpty @Size(min = 15, max = 15) String GST,
                     @NotNull @NotEmpty String companyName, @NotNull @NotEmpty @Size(min = 10, max = 10) String companyContact) {
        super(email, password, confirmPassword);
        this.GST = GST;
        this.companyName = companyName;
        this.companyContact = companyContact;
    }

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
