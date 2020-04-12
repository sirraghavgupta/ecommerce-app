package com.springbootcamp.ecommerceapp.entities;

import com.springbootcamp.ecommerceapp.utils.ProductReviewId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ProductReview {

    @EmbeddedId
    private ProductReviewId productReviewId;

    private String review;
    private Double rating;
    private boolean isDeleted = false;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("customerUserId")
    private Customer author;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("productId")
    private Product product;

    public ProductReview() {
        productReviewId = new ProductReviewId();
    }

    public ProductReview(String review, Double rating) {
        this.review = review;
        this.rating = rating;
        productReviewId = new ProductReviewId();
    }

    @Override
    public String toString() {
        return "ProductReview{" +
                "productReviewId=" + productReviewId +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                '}';
    }

    public void bindCustomer(Customer customer){
        this.setAuthor(customer);
        customer.addReview(this);
    }
    public void bindProduct(Product product){
        this.setProduct(product);
        product.addReview(this);
    }
}
