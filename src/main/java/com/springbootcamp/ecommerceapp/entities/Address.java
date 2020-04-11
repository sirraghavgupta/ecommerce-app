package com.springbootcamp.ecommerceapp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String addressLine;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String label;

    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "customer_user_id")
    private User user;


    public Address(String addressLine, String city, String state, String zipCode, String country, String label) {
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.label = label;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", address='" + addressLine + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", label='" + label + '\'' +
                '}';
    }

// ========= for  many to many purpose
//    public void addUser(User user){
//        if(user!=null){
//            if(users==null)
//                users = new HashSet<User>();
//
//            System.out.println("user added");
//            users.add(user);
//        }
//    }
}
