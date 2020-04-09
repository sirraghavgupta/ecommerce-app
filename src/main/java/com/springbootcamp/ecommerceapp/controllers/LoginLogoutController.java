package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.ForgotPassword;
import com.springbootcamp.ecommerceapp.entities.ForgotPasswordToken;
import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.entities.VerificationToken;
import com.springbootcamp.ecommerceapp.services.UserService;
import com.springbootcamp.ecommerceapp.utils.ResponseVO;
import com.springbootcamp.ecommerceapp.utils.VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@RestController
public class LoginLogoutController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    MessageSource messages;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(){
        return "index";
    }


    @PostMapping("/doLogout")
    public ResponseEntity<VO> logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        VO response = new ResponseVO<String>(null, "You have been logged out successfully", new Date());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<VO> getResetPasswordToken(@RequestBody String email, WebRequest request){
        return userService.initiatePasswordReset(email, request);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<VO> resetPassword(@RequestParam("token") String token, @Valid @RequestBody ForgotPassword passwords, WebRequest request){
        return userService.resetPassword(token, passwords, request);
    }
}
