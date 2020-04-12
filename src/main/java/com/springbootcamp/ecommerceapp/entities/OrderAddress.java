package com.springbootcamp.ecommerceapp.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;


@Data
@NoArgsConstructor
@Embeddable
public class OrderAddress {

    private String addressLine;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String label;

    public OrderAddress(Address address) {
        this.addressLine = address.getAddressLine();
        this.city = address.getCity();
        this.state = address.getState();
        this.zipCode = address.getZipCode();
        this.country = address.getCountry();
        this.label = address.getLabel();
    }
}
