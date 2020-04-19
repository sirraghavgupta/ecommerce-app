package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.CustomerAdminApiDto;
import com.springbootcamp.ecommerceapp.dtos.SellerAdminApiDto;
import com.springbootcamp.ecommerceapp.services.ProductService;
import com.springbootcamp.ecommerceapp.services.CustomerService;
import com.springbootcamp.ecommerceapp.services.SellerService;
import com.springbootcamp.ecommerceapp.services.UserService;
import com.springbootcamp.ecommerceapp.utils.ResponseVO;
import com.springbootcamp.ecommerceapp.utils.BaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
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
    public ResponseEntity<BaseVO> getAdminHome()
    {
        String message = "Admin home";
        BaseVO response = new ResponseVO<>(null, message, new Date());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/customers")
    public ResponseEntity<BaseVO> getAllCustomers(@RequestParam(defaultValue = "0") String offset,
                                                  @RequestParam(defaultValue = "10") String size,
                                                  @RequestParam(defaultValue = "id") String sortByField,
                                                  @RequestParam(defaultValue = "ascending") String order,
                                                  @RequestParam(required = false) String email){

        BaseVO response;
        ResponseEntity<BaseVO> responseEntity;
        List<CustomerAdminApiDto> list = new ArrayList<>();
        if(email!=null){
            CustomerAdminApiDto customerAdminApiDto = customerService.getCustomerByEmail(email);
            if(customerAdminApiDto != null) {
                list.add(customerAdminApiDto);
                response = new ResponseVO<List>(list, null, new Date());
                responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
            }
            else {
                response = new ResponseVO<List>(null, "No user found with this email id.", new Date());
                responseEntity = new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }
        else {
            list = customerService.getAllCustomers(offset, size, sortByField, order);
            response = new ResponseVO<List>(list, null, new Date());
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        }
        return responseEntity;
    }

    @GetMapping("/sellers")
    public ResponseEntity<BaseVO> getAllSellers(@RequestParam(defaultValue = "0") String offset,
                                                @RequestParam(defaultValue = "10") String size,
                                                @RequestParam(defaultValue = "id") String sortByField,
                                                @RequestParam(defaultValue = "ascending") String order,
                                                @RequestParam(required = false) String email) {

        BaseVO response;
        ResponseEntity<BaseVO> responseEntity;
        List<SellerAdminApiDto> list = new ArrayList<>();
        if(email!=null){
            SellerAdminApiDto sellerAdminApiDto = sellerService.getSellerByEmail(email);
            if(sellerAdminApiDto != null) {
                list.add(sellerAdminApiDto);
                response = new ResponseVO<List>(list, null, new Date());
                responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
            }
            else {
                response = new ResponseVO<List>(null, "No user found with this email id.", new Date());
                responseEntity = new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }
        else {
            list = sellerService.getAllSellers(offset, size, sortByField, order);
            response = new ResponseVO<List>(list, null, new Date());
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        }
        return responseEntity;
    }

//    @GetMapping("/products")
//    public ResponseEntity<VO>getAllProducts(){
//        List<Product> list = productService.getAllProducts();
//        VO response = new ResponseVO<>(list, null, new Date());
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }


    @PutMapping("/activate/{id}")
    public ResponseEntity<BaseVO> activateUser(@PathVariable Long id, WebRequest request){
        return userService.activateUserById(id, request);
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<BaseVO> deactivateUser(@PathVariable Long id, WebRequest request){
        return userService.deactivateUserById(id, request);
    }

}
