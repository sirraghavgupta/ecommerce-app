package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.ProductReview;
import com.springbootcamp.ecommerceapp.utils.ProductReviewId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends CrudRepository<ProductReview, ProductReviewId> {
}
