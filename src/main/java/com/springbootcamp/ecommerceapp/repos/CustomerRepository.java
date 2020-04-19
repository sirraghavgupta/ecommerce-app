package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.Customer;
import com.springbootcamp.ecommerceapp.entities.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findById(Long id);

    Customer findByEmail(String email);
    Customer findByEmailAndIsDeletedFalse(String email);

    List<Customer> findAll();
    List<Customer> findAll(Pageable pageable);

    List<Customer> findByIsDeletedFalse();
    List<Customer> findByIsDeletedFalse(Pageable pageable);






}
