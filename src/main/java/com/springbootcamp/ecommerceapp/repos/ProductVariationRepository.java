package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.ProductVariation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariationRepository extends CrudRepository<ProductVariation, Long> {

}
