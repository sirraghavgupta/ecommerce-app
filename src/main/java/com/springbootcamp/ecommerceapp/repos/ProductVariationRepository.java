package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.ProductVariation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariationRepository extends CrudRepository<ProductVariation, Long> {

    List<ProductVariation> findAll();
    List<ProductVariation> findAll(Pageable pageable);

    List<ProductVariation> findByProductId(Long id);
    List<ProductVariation> findByProductId(Long id, Pageable pageable);

}
