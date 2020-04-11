package com.springbootcamp.ecommerceapp.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class CustomerAdminApiDto {

    private Long id;
    private String fullName;
    private String email;
    private boolean isActive;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String firstName, String middleName, String lastName){
        this.fullName = firstName + " " + middleName + " " +lastName;
    }

}
