package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.Customer;
import com.springbootcamp.ecommerceapp.entities.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Customer findByEmail(String email);

    Customer findByEmailAndIsDeletedFalse(String email);

    List<Customer> findAll();
    List<Customer> findAll(Pageable pageable);

//    @Query(value = "select * from customer where is_deleted=false", nativeQuery = true)
    List<Customer> findByIsDeletedFalse();

//    @Query(value = "select * from customer where is_deleted=false", nativeQuery = true)
    List<Customer> findByIsDeletedFalse(Pageable pageable);






}
