package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

}
