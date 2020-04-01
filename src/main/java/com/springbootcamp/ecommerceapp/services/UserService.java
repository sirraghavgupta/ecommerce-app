package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.entities.Seller;
import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.entities.VerificationToken;
import com.springbootcamp.ecommerceapp.events.ActivationEmailFireEvent;
import com.springbootcamp.ecommerceapp.repos.UserRepository;
import com.springbootcamp.ecommerceapp.repos.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    public void createVerificationToken(String token, User user){
        VerificationToken verificationToken = new VerificationToken(token, user);
        tokenRepository.save(verificationToken);
    }

    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    public VerificationToken getVerificationToken(final User user) {
        return tokenRepository.findByUser(user);
    }

    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

//    User registerNewUserAccount(UserDto accountDto) throws EmailExistsException();

    User getUser(String verificationToken){
        VerificationToken token = tokenRepository.findByToken(verificationToken);
        User user = token.getUser();
        return user;
    }

    public void deleteVerificationToken(String token){
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        tokenRepository.delete(verificationToken);
    }

    // activate any user - customer or seller
    public String activateUserById(Long id, WebRequest request) {
        Optional<User> user = userRepository.findById(id);
        String message;
        if(!user.isPresent()){
            message = "user not found";
        }
        else{
            User savedUser = user.get();
            if(savedUser.isActive()){
                message = "user already active";
            }
            else{
                savedUser.setActive(true);
                userRepository.save(savedUser);
                String appUrl = request.getContextPath();
                Locale locale = request.getLocale();
                eventPublisher.publishEvent(new ActivationEmailFireEvent(appUrl, locale, savedUser));
                message = "you have been activated.";
            }
        }
        return message;
    }


    // de-activate any user - customer or seller
    public String deactivateUserById(Long id, WebRequest request) {
        Optional<User> user = userRepository.findById(id);
        String message;
        if(!user.isPresent()){
            message = "user not found";
        }
        else{
            User savedUser = user.get();
            if(!savedUser.isActive()){
                message = "user already inactive";
            }
            else{
                savedUser.setActive(false);
                userRepository.save(savedUser);
                String appUrl = request.getContextPath();
                Locale locale = request.getLocale();
                eventPublisher.publishEvent(new ActivationEmailFireEvent(appUrl, locale, savedUser));
                message = "you have been de-activated.";
            }
        }
        return message;
    }
}
