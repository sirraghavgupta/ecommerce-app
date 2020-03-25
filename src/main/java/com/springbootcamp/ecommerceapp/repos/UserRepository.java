package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

}
