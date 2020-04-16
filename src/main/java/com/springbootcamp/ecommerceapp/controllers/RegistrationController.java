package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.CustomerRegistrationDto;
import com.springbootcamp.ecommerceapp.dtos.SellerRegistrationDto;
import com.springbootcamp.ecommerceapp.repos.CustomerRepository;
import com.springbootcamp.ecommerceapp.repos.SellerRepository;
import com.springbootcamp.ecommerceapp.repos.UserRepository;
import com.springbootcamp.ecommerceapp.services.CustomerService;
import com.springbootcamp.ecommerceapp.services.EmailService;
import com.springbootcamp.ecommerceapp.services.SellerService;
import com.springbootcamp.ecommerceapp.services.UserService;
import com.springbootcamp.ecommerceapp.utils.BaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private UserService userService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    EmailService emailService;

    @PostMapping("/register/customer")
    public ResponseEntity<BaseVO> registerCustomer(@Valid @RequestBody CustomerRegistrationDto customerDto, WebRequest request){
        return userService.createNewCustomer(customerDto, request);
    }


    @GetMapping("/activate/customer")
    public ResponseEntity<BaseVO> activateCustomer(@RequestParam("token") String token, WebRequest request){
        return userService.activateUserByToken(token, request);
    }


    @PostMapping("/resend-activation-link/customer")
    public ResponseEntity<BaseVO> resendActivationLink(@RequestBody String email, WebRequest request){
        return userService.resendActivationLink(email, request);
    }


    @PostMapping("/register/seller")
    public ResponseEntity<BaseVO> registerSeller(@Valid @RequestBody SellerRegistrationDto sellerRegistrationDto){
        return userService.createNewSeller(sellerRegistrationDto);
    }
}
