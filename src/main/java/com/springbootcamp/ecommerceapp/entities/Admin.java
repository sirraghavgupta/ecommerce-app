package com.springbootcamp.ecommerceapp.entities;

import javax.persistence.Entity;

@Entity
public class Admin extends User{

    public Admin() {
    }

    public Admin(String email, String firstName, String middleName, String lastName) {
        super(email, firstName, middleName, lastName);
    }

}
