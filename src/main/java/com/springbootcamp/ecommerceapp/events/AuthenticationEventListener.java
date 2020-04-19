package com.springbootcamp.ecommerceapp.events;

import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import javax.validation.constraints.Null;
import java.util.LinkedHashMap;


@Component
public class AuthenticationEventListener {

    @Autowired
    UserService userService;

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        int counter;
        String userEmail = (String) event.getAuthentication().getPrincipal();

        try{
            User user = userService.getUserByEmail(userEmail);
            userService.registerUnsuccessfulLogin(user);
            userService.saveRegisteredUser(user);
        }catch(NullPointerException ex){
            System.out.println(ex);
        }
    }

    @EventListener
    public void authenticationPass(AuthenticationSuccessEvent event) {

        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {

            System.out.println("====================== event triggered =======================");

            LinkedHashMap<String,String> userMap = new LinkedHashMap<>();
            try {
                userMap = (LinkedHashMap<String, String>) event.getAuthentication().getDetails();
            } catch (ClassCastException ex) {
                System.out.println(ex);
            }

            String userEmail = new String();

            try {
                userEmail = userMap.get("username");
            } catch (NullPointerException ex) {
                System.out.println(ex);
            }

        User user = userService.getUserByEmail(userEmail);
        try{
            userService.registerSuccessfulLogin(user);
            userService.saveRegisteredUser(user);
        }
        catch(NullPointerException ex){
            System.out.println(ex);
        }
      }
    }
}

