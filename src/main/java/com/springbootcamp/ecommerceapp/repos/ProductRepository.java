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
import java.util.Set;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findAll();
    List<Product> findAll(Pageable pageable);

    Product findByName(String name);
    List<Product> findByBrandAndCategoryId(String brand, Long id);
    List<Product> findByBrand(String brand);
    List<Product> findByCategoryId(Long id);

    List<Product> findByBrandAndCategoryId(String brand, Long id, Pageable pageable);
    List<Product> findByBrand(String brand, Pageable pageable);
    List<Product> findByCategoryId(Long id, Pageable pageable);

    @Query(value = "select brand from product where category_id = :c_id", nativeQuery = true)
    List<String> findBrandByCategoryId(@Param("c_id") Long c_id);

    @Modifying
    @Transactional
    @Query(value = "delete from product where id = :p_id", nativeQuery = true)
    void deleteProductById(@Param("p_id") Long p_id);

    @Query(value = "select brand from product where category_id = :c_id", nativeQuery = true)
    List<String> findAllBrandsByCategoryId(@Param("c_id") Long c_id);
}
