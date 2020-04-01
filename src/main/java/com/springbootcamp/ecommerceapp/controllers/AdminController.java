package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.CustomerAdminApiDto;
import com.springbootcamp.ecommerceapp.dtos.CustomerRegistrationDto;
import com.springbootcamp.ecommerceapp.dtos.SellerAdminApiDto;
import com.springbootcamp.ecommerceapp.dtos.SellerRegistrationDto;
import com.springbootcamp.ecommerceapp.entities.Customer;
import com.springbootcamp.ecommerceapp.entities.Product;
import com.springbootcamp.ecommerceapp.entities.Seller;
import com.springbootcamp.ecommerceapp.services.ProductService;
import com.springbootcamp.ecommerceapp.services.CustomerService;
import com.springbootcamp.ecommerceapp.services.SellerService;
import com.springbootcamp.ecommerceapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;


    @GetMapping("/admin/home")
    public String adminHome(){
        return "Admin home";
    }

    @GetMapping("/customers")
    public List<CustomerAdminApiDto> getAllCustomers(@RequestParam(defaultValue = "0") String offset,
                                                     @RequestParam(defaultValue = "10") String size,
                                                     @RequestParam(defaultValue = "id") String sortByField,
                                                     @RequestParam(required = false) String email){
        if(email!=null)
            return Arrays.asList(customerService.getCustomerByEmail(email));

        return customerService.getAllCustomers(offset, size, sortByField);
    }

    @GetMapping("/sellers")
    public List<SellerAdminApiDto> getAllSellers(@RequestParam(defaultValue = "0") String offset,
                                                 @RequestParam(defaultValue = "10") String size,
                                                 @RequestParam(defaultValue = "id") String sortByField,
                                                 @RequestParam(required = false) String email) {

        if(email!=null)
            return Arrays.asList(sellerService.getSellerByEmail(email));

        return sellerService.getAllSellers(offset, size, sortByField);
    }

    @GetMapping("/products")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }


    @PutMapping("/activate/{id}")
    public String activateUser(@PathVariable Long id, WebRequest request){
        String message = userService.activateUserById(id, request);
        return message;
    }

    @PutMapping("/deactivate/{id}")
    public String deactivateUser(@PathVariable Long id, WebRequest request){
        String message = userService.deactivateUserById(id, request);
        return message;
    }

}
