package com.springbootcamp.ecommerceapp.security;

import com.springbootcamp.ecommerceapp.repos.UserRepository;
import com.springbootcamp.ecommerceapp.security.AppUser;
import com.springbootcamp.ecommerceapp.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Repository
@Service
public class UserDao {

    @Autowired
    UserRepository userRepository;

    AppUser loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return new AppUser(user);
        } else {
            throw new UsernameNotFoundException("user  " + user.getEmail() + " was not found");
        }
    }

}
