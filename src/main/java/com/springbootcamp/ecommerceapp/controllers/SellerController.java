package com.springbootcamp.ecommerceapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellerController {

    @Autowired
    private TokenStore tokenStore;

    @GetMapping("/seller/home")
    public String sellerHome(){
        return "Seller home";
    }
}
