package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.utils.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class UserController {

    @Autowired
    private TokenStore tokenStore;

    @GetMapping("/user/home")
    public ResponseEntity<ResponseVO> userHome(){
        String data = "user home";
        ResponseVO<String> response = new ResponseVO<>(data, null, new Date());
        return new ResponseEntity<ResponseVO>(response, HttpStatus.OK);
    }
}
