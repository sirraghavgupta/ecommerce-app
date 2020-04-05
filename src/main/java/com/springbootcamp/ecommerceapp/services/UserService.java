package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.dtos.ForgotPassword;
import com.springbootcamp.ecommerceapp.entities.ForgotPasswordToken;
import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.entities.VerificationToken;
import com.springbootcamp.ecommerceapp.events.ActivationEmailFireEvent;
import com.springbootcamp.ecommerceapp.repos.ForgotPasswordRepository;
import com.springbootcamp.ecommerceapp.repos.UserRepository;
import com.springbootcamp.ecommerceapp.repos.VerificationTokenRepository;
import com.springbootcamp.ecommerceapp.validators.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserService {

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    MessageSource messages;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    EmailValidator emailValidator;

    @Autowired
    private TokenStore tokenStore;

    public void createVerificationToken(String token, User user){
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    public void createForgotPasswordToken(String token, User user){
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken(token, user);
        forgotPasswordRepository.save(forgotPasswordToken);
    }

    public VerificationToken getVerificationToken(final String VerificationToken) {
        return verificationTokenRepository.findByToken(VerificationToken);
    }

    public ForgotPasswordToken getForgotPasswordToken(final User user) {
        return forgotPasswordRepository.findByUser(user);
    }

    public ForgotPasswordToken getForgotPasswordToken(final String forgotPasswordToken) {
        return forgotPasswordRepository.findByToken(forgotPasswordToken);
    }

    public VerificationToken getVerificationToken(final User user) {
        return verificationTokenRepository.findByUser(user);
    }

    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

    User getUser(String verificationToken){
        VerificationToken token = verificationTokenRepository.findByToken(verificationToken);
        User user = token.getUser();
        return user;
    }

    public void deleteVerificationToken(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        verificationTokenRepository.delete(verificationToken);
    }

    public void deleteForgotPasswordToken(String token){
        ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);
        forgotPasswordRepository.delete(forgotPasswordToken);
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

    public String initiatePasswordReset(String email, WebRequest request){
        if(!isValidEmail(email))
            return "invalid email";

        User user = userRepository.findByEmail(email);
        String message;
        if(user==null)
            throw new UsernameNotFoundException("this email address doesn't exist");

        else if(!user.isActive())
            message = "user is de-activated";

        // user is activated - eligible fot this operation.
        else{
            // create and save the token
            String token = UUID.randomUUID().toString();
            createForgotPasswordToken(token, user);

            // prepare the email contents
            String recipientAddress = user.getEmail();
            String subject = "Forgot Password";
            String confirmationUrl = "/reset-password?token=" + token;
            String emailMessage = "please click on this link to reset your password";

            System.out.println(confirmationUrl);

            // populate the email and send
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(recipientAddress);
            mail.setSubject(subject);
            String newUrl = "http://localhost:8080" + confirmationUrl ;
            mail.setText(emailMessage + "\r\n" + newUrl);
            System.out.println(newUrl);

            mailSender.send(mail);

            message = messages.getMessage("message.resendToken", null, request.getLocale());
        }
        return message;
    }

    public boolean isValidEmail(String email){
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public User getUserByEmail(String email){
        User user = userRepository.findByEmail(email);

        if(user==null)
            System.out.println("user not found");

        return user;
    }

}
