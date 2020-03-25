package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.repos.UserRepository;
import com.springbootcamp.ecommerceapp.entities.AppUser;
import com.springbootcamp.ecommerceapp.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

//@Repository
@Service
public class UserDao {

    @Autowired
    UserRepository userRepository;

    AppUser loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        System.out.println(user);
        if (email != null) {
            return new AppUser(user);
        } else {
            throw new RuntimeException();
        }

    }


//    public User createUser(User user){
//        String username = user.getUsername();
//
//        if(userRepository.findByUsername(username) != null){
//            userRepository.save(user);
//        }
//        else {
//            System.out.println("user already exists");
//        }
//        return user;
//    }


}
