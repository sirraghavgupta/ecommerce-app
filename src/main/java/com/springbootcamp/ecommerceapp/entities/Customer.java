package com.springbootcamp.ecommerceapp.entities;

import javax.persistence.*;

@Entity
public class Customer extends User{

    private String contact;

    public Customer(){
        this.addRole(new Role(3, "ROLE_CUSTOMER"));
    }

    public Customer(String email, String firstName, String middleName, String lastName, String contact) {
        super(email, firstName, middleName, lastName);
        this.addRole(new Role(3, "ROLE_CUSTOMER"));
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Customer{" +
                super.toString() +
                "contact='" + contact + '\'' +
                '}';
    }
}
