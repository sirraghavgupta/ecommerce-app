//package com.springbootcamp.ecommerceapp.entities;
//
//import javax.persistence.Embeddable;
//
//@Embeddable
//public class OrderAddress {
//
//    private String addressLine;
//    private String city;
//    private String state;
//    private String zipCode;
//    private String country;
//    private String label;
//
//    public OrderAddress() {
//    }
//
//    public OrderAddress(Address address) {
//        this.addressLine = address.getAddressLine();
//        this.city = address.getCity();
//        this.state = address.getState();
//        this.zipCode = address.getZipCode();
//        this.country = address.getCountry();
//        this.label = address.getLabel();
//    }
//
//    public String getAddressLine() {
//        return addressLine;
//    }
//
//    public void setAddressLine(String addressLine) {
//        this.addressLine = addressLine;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public String getZipCode() {
//        return zipCode;
//    }
//
//    public void setZipCode(String zipCode) {
//        this.zipCode = zipCode;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public void setLabel(String label) {
//        this.label = label;
//    }
//
//    @Override
//    public String toString() {
//        return "OrderAddress{" +
//                "addressLine='" + addressLine + '\'' +
//                ", city='" + city + '\'' +
//                ", state='" + state + '\'' +
//                ", zipCode='" + zipCode + '\'' +
//                ", country='" + country + '\'' +
//                ", label='" + label + '\'' +
//                '}';
//    }
//}
