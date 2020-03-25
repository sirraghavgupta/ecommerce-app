package com.springbootcamp.ecommerceapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AdminController {

    @Autowired
    private TokenStore tokenStore;

    @GetMapping("/admin/home")
    public String adminHome(){
        return "Admin home";
    }
}