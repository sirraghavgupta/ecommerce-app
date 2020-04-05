package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.UserRegistrationDto;
import com.springbootcamp.ecommerceapp.entities.Product;
import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.repos.ProductRepository;
import com.springbootcamp.ecommerceapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserService userService;

    @GetMapping("/customer/home")
    public List<Product> customerHome(){

        List<Product> products = (List)productRepository.findAll();
        return products;

    }

    // just for testing
    @GetMapping("/user-test")
    public void getUser(){
        User user = userService.getUserByEmail("draghavgupta.96@gmail.com");
        System.out.println("######### " + user);
    }
}
