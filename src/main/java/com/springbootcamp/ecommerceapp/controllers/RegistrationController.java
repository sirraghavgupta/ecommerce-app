package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.CustomerRegistrationDto;
import com.springbootcamp.ecommerceapp.dtos.SellerRegistrationDto;
import com.springbootcamp.ecommerceapp.entities.Seller;
import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.entities.VerificationToken;
import com.springbootcamp.ecommerceapp.repos.CustomerRepository;
import com.springbootcamp.ecommerceapp.repos.SellerRepository;
import com.springbootcamp.ecommerceapp.repos.UserRepository;
import com.springbootcamp.ecommerceapp.services.CustomerService;
import com.springbootcamp.ecommerceapp.services.EmailService;
import com.springbootcamp.ecommerceapp.services.SellerService;
import com.springbootcamp.ecommerceapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;

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
    public String registerCustomer(@Valid @RequestBody CustomerRegistrationDto customerDto, WebRequest request){

        String message = userService.createNewCustomer(customerDto, request);
        return message;
    }


    @GetMapping("/activate/customer")
    public String activateCustomer(@RequestParam("token") String token, WebRequest request){
        Locale locale = request.getLocale();

        // if token doesnt exist in database
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            return messages.getMessage("auth.message.invalidToken", null, locale);
        }

        // if token is expired
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {

            String appUrl = request.getContextPath();
            userService.deleteVerificationToken(token);
            userService.sendActivationLinkMail(appUrl, user, locale, "Account Activation Link");
            return messages.getMessage("auth.message.expired", null, locale);
        }

        // if everything is alright
        if(user.isActive())
            return "your account is already active";

        user.setActive(true);
        userService.saveRegisteredUser(user);
        userService.deleteVerificationToken(token);
        return "you have been activated successfully";
    }


    @PostMapping("/resend-activation-link/customer")
    public String resendActivationLink(@RequestBody String email, WebRequest request){
        User user = userRepository.findByEmail(email);
        String appUrl = request.getContextPath();
        Locale locale = request.getLocale();

        if(user==null)
            return messages.getMessage("ValidEmail.user.email", null, locale);

        VerificationToken token = userService.getVerificationToken(user);
        if(token==null)
            return "user already activated";

        userService.deleteVerificationToken(token.getToken());
        userService.sendActivationLinkMail(appUrl, user, locale, "Account Activation Link");
        return messages.getMessage("message.resendToken", null, locale);
    }


    @PostMapping("/register/seller")
    public String registerSeller(@Valid @RequestBody SellerRegistrationDto sellerRegistrationDto){

        String message = userService.createNewSeller(sellerRegistrationDto);
        return message;
    }
}
