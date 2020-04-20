package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.AddressDto;
import com.springbootcamp.ecommerceapp.dtos.SellerViewProfileDto;
import com.springbootcamp.ecommerceapp.services.SellerService;
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

@RestController
public class SellerController {

    @Autowired
    SellerService sellerService;

    @Autowired
    UserService userService;

    @GetMapping("/seller/home")
    public ResponseEntity<ResponseVO> getsellerHome(){
        String data = "seller home";
        ResponseVO<String> response = new ResponseVO<>(data, null, new Date());
        return new ResponseEntity<ResponseVO>(response, HttpStatus.OK);
    }

    @GetMapping("/seller/profile")
    public ResponseEntity<BaseVO> getProfileDetails(HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return sellerService.getUserProfile(username);
    }

    @PatchMapping("/seller/profile")
    public ResponseEntity<BaseVO> updateProfileDetails(@Valid @RequestBody SellerViewProfileDto profileDto, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return sellerService.updateUserProfile(username, profileDto);
    }

    @PatchMapping("/seller/addresses/{id}")
    public ResponseEntity<BaseVO> updateAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable Long id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return userService.updateAddressById(username, id, addressDto);
    }
}
