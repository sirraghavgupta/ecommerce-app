package com.springbootcamp.ecommerceapp.utils;

import com.springbootcamp.ecommerceapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import javax.swing.text.html.Option;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    UserService userService;

    @Override
    public Optional<String> getCurrentAuditor() {
        // your custom logic
        Optional<String> currentUser = Optional.empty();
        String principal = userService.getCurrentLoggedInUser();
        currentUser = Optional.of(principal);
        return currentUser;
    }


}