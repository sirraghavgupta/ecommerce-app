package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
