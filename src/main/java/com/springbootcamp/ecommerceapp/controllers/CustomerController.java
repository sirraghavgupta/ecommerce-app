package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.entities.Product;
import com.springbootcamp.ecommerceapp.repos.ProductRepository;
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

    @GetMapping("/customer/home")
//    public String customerHome(){
    public List<Product> customerHome(){

        List<Product> products = (List)productRepository.findAll();
        return products;

//        return "Customer Home";
    }
}
