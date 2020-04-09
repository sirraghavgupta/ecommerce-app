package com.springbootcamp.ecommerceapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.*;

@Entity
public class Customer extends User{

    private String contact;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductReview> reviews;

//    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
//    private Set<Order> orders;


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

    public List<ProductReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<ProductReview> reviews) {
        this.reviews = reviews;
    }

//    public Set<Order> getOrders() {
//        return orders;
//    }
//
//    public void setOrders(Set<Order> orders) {
//        this.orders = orders;
//    }

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

//    public void addOrder(Order order){
//        if(order!=null){
//            if(orders==null){
//                orders = new LinkedHashSet<>();
//            }
//            orders.add(order);
//            order.setCustomer(this);
//        }
//    }

}
