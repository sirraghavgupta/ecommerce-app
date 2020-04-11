package com.springbootcamp.ecommerceapp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String review;
    private Double rating;

    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "customer_user_id")
    private Customer author;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public ProductReview(String review, Double rating) {
        this.review = review;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ProductReview{" +
                "id=" + id +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                "posted by=" + author.getEmail() +
                '}';
    }
}
