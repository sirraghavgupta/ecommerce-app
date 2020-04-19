package com.springbootcamp.ecommerceapp.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Where(clause = "is_deleted=false")
public class Product  extends AuditInformation{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private String brand;

    private Boolean isReturnable =false;
    private Boolean isCancelleable = false;
    private Boolean isActive = false;
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "seller_user_id")
    private Seller seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductVariation> variations;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductReview> reviews;


    public Product(String name, String description, String brand) {
        this.name = name;
        this.description = description;
        this.brand = brand;
    }


    public void addVariation(ProductVariation variation){
        if(variation != null){
            if(variations == null)
                variations = new HashSet<>();

            variations.add(variation);
            variation.setProduct(this);
        }
    }

    public void addReview(ProductReview review){
        if(review != null){
            if(reviews == null)
                reviews = new ArrayList<>();

            reviews.add(review);
            review.setProduct(this);
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", brand='" + brand + '\'' +
                ", isReturnable=" + isReturnable +
                ", isCancelleable=" + isCancelleable +
                ", isActive=" + isActive +
                ", isDeleted=" + isDeleted +
                ", seller=" + seller.getCompanyName() +
                ", variations=" + variations.size() +
                ", category=" + category.getName() +
                ", reviews=" + reviews.size() +
                '}';
    }
}
