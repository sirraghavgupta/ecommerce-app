package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends CrudRepository<Seller, Long> {

    Optional<Seller> findById(Long id);

    Seller findByEmail(String email);
    Seller findByEmailAndIsDeletedFalse(String email);

    Seller findByCompanyName(String companyName);
    Seller findByCompanyNameAndIsDeletedFalse(String companyName);


    Seller findByGST(String GST);
    Seller findByGSTAndIsDeletedFalse(String GST);

    List<Seller> findByIsDeletedFalse();
    List<Seller> findByIsDeletedFalse(Pageable pageable);

}
