package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.Seller;
import org.springframework.data.repository.CrudRepository;

public interface SellerRepository extends CrudRepository<Seller, Long> {

    Seller findByEmail(String email);

    Seller findByCompanyName(String companyName);

    Seller findByGST(String GST);

}
