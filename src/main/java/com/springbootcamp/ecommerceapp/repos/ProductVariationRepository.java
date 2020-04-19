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
import java.util.Optional;

@Repository
public interface ProductVariationRepository extends CrudRepository<ProductVariation, Long> {

    Optional<ProductVariation> findByIdAndIsDeletedFalse(Long id);

    List<ProductVariation> findAll();
    List<ProductVariation> findAllByIsDeletedFalse();

    List<ProductVariation> findAll(Pageable pageable);
    List<ProductVariation> findAllByIsDeletedFalse(Pageable pageable);

    List<ProductVariation> findByProductId(Long id);
    List<ProductVariation> findByProductIdAndIsDeletedFalse(Long id);

    List<ProductVariation> findByProductId(Long id, Pageable pageable);
    List<ProductVariation> findByProductIdAndIsDeletedFalse(Long id, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update product_variation set is_deleted=true where product_id = :p_id", nativeQuery = true)
    void deleteByProductId(@Param("p_id") Long p_id);

    @Modifying
    @Transactional
    @Query(value = "update product_variation set is_deleted=true where id = :v_id", nativeQuery = true)
    void deleteVariationById(@Param("v_id") Long v_id);


}
