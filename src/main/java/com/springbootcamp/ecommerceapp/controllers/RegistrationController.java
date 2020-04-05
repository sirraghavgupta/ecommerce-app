package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.exception.EmailAlreadyExistsException;
import com.springbootcamp.ecommerceapp.dtos.CustomerRegistrationDto;
import com.springbootcamp.ecommerceapp.dtos.SellerRegistrationDto;
import com.springbootcamp.ecommerceapp.entities.Customer;
import com.springbootcamp.ecommerceapp.entities.Seller;
import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.entities.VerificationToken;
import com.springbootcamp.ecommerceapp.events.ActivationEmailFireEvent;
import com.springbootcamp.ecommerceapp.repos.CustomerRepository;
import com.springbootcamp.ecommerceapp.repos.SellerRepository;
import com.springbootcamp.ecommerceapp.repos.UserRepository;
import com.springbootcamp.ecommerceapp.services.CustomerService;
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
import java.util.List;
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

    @PostMapping("/register/customer")
    public String registerCustomer(@Valid @RequestBody CustomerRegistrationDto cust, WebRequest request){

        Customer customer = customerRepository.findByEmail(cust.getEmail());

        if(customer != null)
            throw new EmailAlreadyExistsException("email id already exists");

        else{
            Customer newCustomer = customerService.toCustomer(cust);
            Customer savedCustomer = customerRepository.save(newCustomer);
            System.out.println("customer registered successfully.");

//            try {
                String appUrl = request.getContextPath();
                eventPublisher.publishEvent(new ActivationEmailFireEvent
                        (appUrl, request.getLocale(), savedCustomer));
//            }
//            catch (Exception me) {
//                return new ModelAndView("emailError", "user", accountDto);
//            }
            return "success";
        }
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
            eventPublisher.publishEvent(new ActivationEmailFireEvent(appUrl, locale, user));

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
        eventPublisher.publishEvent(new ActivationEmailFireEvent(appUrl, locale, user));
        return messages.getMessage("message.resendToken", null, locale);
    }


    @PostMapping("/register/seller")
    public String registerSeller(@Valid @RequestBody SellerRegistrationDto sellerRegistrationDto){

        String message = sellerService.getUniquenessStatus(sellerRegistrationDto);
        if(!message.equals("unique"))
            return message;

        Seller seller = sellerService.toSeller(sellerRegistrationDto);
        sellerRepository.save(seller);
        sellerService.sendAcknowledgementMail(seller.getEmail());
        return "success";
    }
}
