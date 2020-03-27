package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.ProductReview;
import org.springframework.data.repository.CrudRepository;

public interface ProductReviewRepository extends CrudRepository<ProductReview, Long> {
}
