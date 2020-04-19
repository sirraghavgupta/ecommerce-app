package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.ProductReview;
import com.springbootcamp.ecommerceapp.utils.ProductReviewId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ProductReviewRepository extends CrudRepository<ProductReview, ProductReviewId> {

    @Modifying
    @Transactional
    @Query(value = "update product_review set is_deleted=true where product_id = :p_id", nativeQuery = true)
    void deleteByProductId(@Param("p_id") Long p_id);
}
