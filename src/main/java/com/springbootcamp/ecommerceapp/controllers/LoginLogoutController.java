package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.ForgotPassword;
import com.springbootcamp.ecommerceapp.entities.ForgotPasswordToken;
import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.entities.VerificationToken;
import com.springbootcamp.ecommerceapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
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
    public String logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return "Logged out successfully";
    }


    @PostMapping("/forgot-password")
    public String getResetPasswordToken(@RequestBody String email, WebRequest request){
        String message = userService.initiatePasswordReset(email, request);
        return message;
    }


    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @Valid @RequestBody ForgotPassword passwords, WebRequest request){

        Locale locale = request.getLocale();

        // if token doesn't exist in database
        ForgotPasswordToken forgotPasswordToken = userService.getForgotPasswordToken(token);
        if (forgotPasswordToken == null) {
            return messages.getMessage("auth.message.invalidToken", null, locale);
        }

        // if token is expired
        User user = forgotPasswordToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((forgotPasswordToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {

            String appUrl = request.getContextPath();
            userService.deleteForgotPasswordToken(token);
            return messages.getMessage("auth.message.expired", null, locale);
        }

        user.setPassword(passwords.getPassword());
        userService.saveRegisteredUser(user);
        userService.deleteForgotPasswordToken(token);

        // logout the user of all active sessions, if any - delete the oAuth token
        userService.logoutUser(user.getEmail(), request);
        userService.sendPasswordResetConfirmationMail(user.getEmail());
        return "password changed successfully";
    }
}
