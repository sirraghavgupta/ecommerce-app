package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.AddressDto;
import com.springbootcamp.ecommerceapp.dtos.CustomerViewProfileDto;
import com.springbootcamp.ecommerceapp.entities.Product;
import com.springbootcamp.ecommerceapp.repos.ProductRepository;
import com.springbootcamp.ecommerceapp.services.CustomerService;
import com.springbootcamp.ecommerceapp.services.UserService;
import com.springbootcamp.ecommerceapp.utils.ResponseVO;
import com.springbootcamp.ecommerceapp.utils.BaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserService userService;

    @Autowired
    CustomerService customerService;

    @GetMapping("/customer/home")
    public ResponseEntity<ResponseVO> getCustomerHome(){

        List<Product> products = (List)productRepository.findAll();
        ResponseVO<List> response = new ResponseVO<>(products, null, new Date());
        return new ResponseEntity<ResponseVO>(response, HttpStatus.OK);
    }

    @GetMapping("/customer/profile")
    public ResponseEntity<BaseVO> getProfileDetails(HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return customerService.getUserProfile(username);
    }

    @PatchMapping("/customer/profile")
    public ResponseEntity<BaseVO> updateProfileDetails(@Valid @RequestBody CustomerViewProfileDto profileDto, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return customerService.updateUserProfile(username, profileDto);
    }

    @GetMapping("/customer/addresses")
    public ResponseEntity<BaseVO> getCustomerAddresses(HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return customerService.getCustomerAddresses(username);
    }

    @PostMapping("/customer/addresses")
    public ResponseEntity<BaseVO> addNewAddress(@Valid @RequestBody AddressDto addressDto, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return customerService.addNewAddress(username, addressDto);
    }

    @DeleteMapping("/customer/addresses/{id}")
    public ResponseEntity<BaseVO> deleteAddressById(@PathVariable Long id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return customerService.deleteAddress(username, id);
    }

    @PatchMapping("/customer/addresses/{id}")
    public ResponseEntity<BaseVO> updateAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable Long id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return userService.updateAddressById(username, id, addressDto);
    }
}
