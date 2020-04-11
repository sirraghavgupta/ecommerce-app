package com.springbootcamp.ecommerceapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerAdminApiDto {

    private Long id;
    private String fullName;
    private String email;
    private boolean isActive;

    private String GST;
    private String companyName;
    private String companyContact;

    public void setFullName(String firstName, String middleName, String lastName){
        this.fullName = firstName + " " + middleName + " " +lastName;
    }
}
