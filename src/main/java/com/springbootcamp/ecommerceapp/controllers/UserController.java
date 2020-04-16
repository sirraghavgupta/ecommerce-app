package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.ForgotPasswordDto;
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
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user/home")
    public ResponseEntity<ResponseVO> userHome(){
        String data = "user home";
        ResponseVO<String> response = new ResponseVO<>(data, null, new Date());
        return new ResponseEntity<ResponseVO>(response, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<BaseVO> changePassword(@Valid @RequestBody ForgotPasswordDto passwords, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return userService.changePassword(username, passwords);
    }

}
