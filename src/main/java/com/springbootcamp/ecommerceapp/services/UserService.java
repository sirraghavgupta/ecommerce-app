package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.entities.Seller;
import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.entities.VerificationToken;
import com.springbootcamp.ecommerceapp.repos.UserRepository;
import com.springbootcamp.ecommerceapp.repos.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

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

}
