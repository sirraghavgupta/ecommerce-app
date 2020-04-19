package com.springbootcamp.ecommerceapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Where(clause = "is_deleted=false")
public class Customer extends User{

    private String contact;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductReview> reviews;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Orders> orders;

    @OneToOne(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Cart cart;

    public Customer(){
        this.addRole(new Role(3, "ROLE_CUSTOMER"));
    }

    public Customer(String email, String firstName, String middleName, String lastName, String contact) {
        super(email, firstName, middleName, lastName);
        this.addRole(new Role(3, "ROLE_CUSTOMER"));
        this.contact = contact;
    }

    public void addReview(ProductReview review){
        if(review != null){
            if(reviews == null)
                 reviews = new ArrayList<>();

            reviews.add(review);
            review.setAuthor(this);
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                super.toString() +
                "contact='" + contact + '\'' +
                '}';
    }

    public void addOrder(Orders order){
        if(order!=null){
            if(orders==null){
                orders = new LinkedHashSet<>();
            }
            orders.add(order);
            order.setCustomer(this);
        }
    }
}
