package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.ProductVariation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductVariationRepository extends CrudRepository<ProductVariation, Long> {

    List<ProductVariation> findAll();
    List<ProductVariation> findAll(Pageable pageable);

    List<ProductVariation> findByProductId(Long id);
    List<ProductVariation> findByProductId(Long id, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "delete from product_variation where product_id = :p_id", nativeQuery = true)
    void deleteByProductId(@Param("p_id") Long p_id);

    @Query(value = "SELECT * FROM hibernate_sequence limit 1", nativeQuery = true)
    BigDecimal getNextValMySequence();
}
