package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByIdAndIsDeletedFalse(Long id);

    List<Product> findAll();
    List<Product> findAll(Pageable pageable);

    List<Product> findByIsDeletedFalse();
    List<Product> findByIsDeletedFalse(Pageable pageable);
    List<Product> findBySellerIdAndIsDeletedFalse(Long s_id, Pageable pageable);



    Product findByName(String name);
    Product findByNameAndIsDeletedFalse(String name);

    List<Product> findByBrandAndCategoryIdAndIsDeletedFalse(String brand, Long id);
    List<Product> findByBrandAndIsDeletedFalse(String brand);

    List<Product> findByCategoryIdAndIsDeletedFalse(Long c_id);
    List<Product> findBySellerIdAndCategoryIdAndIsDeletedFalse(Long s_id, Long c_id);

    List<Product> findByBrandAndCategoryIdAndIsDeletedFalse(String brand, Long c_id, Pageable pageable);
    List<Product> findBySellerIdAndBrandAndCategoryIdAndIsDeletedFalse(Long s_id, String brand, Long c_id, Pageable pageable);

    List<Product> findByBrandAndIsDeletedFalse(String brand, Pageable pageable);
    List<Product> findBySellerIdAndBrandAndIsDeletedFalse(Long s_id, String brand, Pageable pageable);

    List<Product> findByCategoryIdAndIsDeletedFalse(Long c_id, Pageable pageable);
    List<Product> findBySellerIdAndCategoryIdAndIsDeletedFalse(Long s_id, Long c_id, Pageable pageable);



    @Query(value = "select brand from product where category_id = :c_id and is_deleted=false", nativeQuery = true)
    List<String> findAllBrandsByCategoryId(@Param("c_id") Long c_id);

    @Modifying
    @Transactional
    @Query(value = "update product set is_deleted=true where id = :p_id", nativeQuery = true)
    void deleteProductById(@Param("p_id") Long p_id);

    @Modifying
    @Transactional
    @Query(value = "update product set is_deleted=true where category_id = :c_id", nativeQuery = true)
    void deleteProductsByCategoryId(@Param("c_id") Long c_id);


}
