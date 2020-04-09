package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.utils.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class SellerController {

    @Autowired
    private TokenStore tokenStore;

    @GetMapping("/seller/home")
    public ResponseEntity<ResponseVO> getsellerHome(){
        String data = "seller home";
        ResponseVO<String> response = new ResponseVO<>(data, null, new Date());
        return new ResponseEntity<ResponseVO>(response, HttpStatus.OK);
    }
}
