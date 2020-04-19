package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findById(Long id);
    Optional<User> findByIdAndIsDeletedFalse(Long id);

    User findByEmail(String email);

    User findByEmailAndIsDeletedFalse(String email);


}

