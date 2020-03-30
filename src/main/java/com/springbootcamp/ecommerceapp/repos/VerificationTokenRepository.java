package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.entities.VerificationToken;
import org.springframework.data.repository.CrudRepository;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

}
