package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.dtos.CustomerRegistrationDto;
import com.springbootcamp.ecommerceapp.dtos.SellerRegistrationDto;
import com.springbootcamp.ecommerceapp.entities.*;
import com.springbootcamp.ecommerceapp.exception.EmailAlreadyExistsException;
import com.springbootcamp.ecommerceapp.repos.*;
import com.springbootcamp.ecommerceapp.validators.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
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
    CustomerRepository customerRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    SellerService sellerService;

    @Autowired
    EmailService emailService;

    @Autowired
    MessageSource messages;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    EmailValidator emailValidator;

    @Autowired
    private TokenStore tokenStore;

    public String createVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public String createForgotPasswordToken(User user){
        String token = UUID.randomUUID().toString();
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken(token, user);
        forgotPasswordRepository.save(forgotPasswordToken);
        return token;
    }

    public VerificationToken getVerificationToken(final User user) {
        return verificationTokenRepository.findByUser(user);
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

    public void deleteVerificationToken(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        verificationTokenRepository.delete(verificationToken);
    }

    public void deleteForgotPasswordToken(String token){
        ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);
        forgotPasswordRepository.delete(forgotPasswordToken);
    }

    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

    User getUser(String verificationToken){
        VerificationToken token = verificationTokenRepository.findByToken(verificationToken);
        return token.getUser();
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
                Locale locale = request.getLocale();
                String subject = "Account Activation";
                message = "your account has been activated.";
                emailService.sendEmail(savedUser.getEmail(), subject, message);
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
                Locale locale = request.getLocale();
                message = "your account has been de-activated.";
                String subject = "Account De-activation";
                emailService.sendEmail(savedUser.getEmail(), subject, message);
            }
        }
        return message;
    }

    public String initiatePasswordReset(String email, WebRequest request){
        String message;

        // check uniqueness of email
        User user = userRepository.findByEmail(email);
        if(user==null)
            throw new UsernameNotFoundException("this email address doesn't exist");

        else if(!user.isActive() || user.isLocked())
            message = "user is either de-activated or locked";

        // user is activated - eligible for this operation.
        else{
            String token =createForgotPasswordToken(user);
            sendForgotPasswordInitiationMail(user, token);
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

    public void logoutUser(String email, WebRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
    }

    public void registerUnsuccessfulLogin(User user){
        int count = user.getFailedAttempts();
        user.setFailedAttempts(count+1);
        if(user.getFailedAttempts() >= 3){
            user.setLocked(true);
            sendAccountLockingMail(user.getEmail());
        }
    }

    private void sendAccountLockingMail(String email) {
        String subject = "Account Locked";
        String message = "your account has been locked due to multiple unsuccessful login attempts.";
        emailService.sendEmail(email, subject, message);
    }

    public void registerSuccessfulLogin(User user){
        user.setFailedAttempts(0);
    }

    public void sendActivationLinkMail(String appUrl, User user, Locale locale, String subject){

        // create and save the token
        String token = createVerificationToken(user);

        // prepare the email contents
        String email = user.getEmail();
        String confirmationUrl = "http://localhost:8080" + appUrl +
                "/activate/customer?token=" + token;

//        String message = messages.getMessage("message.regSucc", null, locale);
        String message = "please activate your account";
        System.out.println(confirmationUrl);

        // populate the email and send
        emailService.sendEmail(email, subject, message);
    }

    public void sendForgotPasswordInitiationMail(User user, String token){

        String email = user.getEmail();
        String subject = "Password Reset Link";
        String passwordResetUrl = "http://localhost:8080" + "/reset-password?token=" + token;
        String emailMessage = "please click on this link to reset your password";
        String emailBody = emailMessage + "\r\n" + passwordResetUrl;
        System.out.println(passwordResetUrl);

        emailService.sendEmail(email, subject, emailBody);
    }

    public void sendPasswordResetConfirmationMail(String email) {
        String subject = "Password Reset Successfully";
        String message = "the password for yout account has been reset successfully";
        emailService.sendEmail(email, subject, message);
    }

    public String createNewCustomer(CustomerRegistrationDto customerDto, WebRequest request){
        Customer customer = customerRepository.findByEmail(customerDto.getEmail());

        if(customer != null)
            throw new EmailAlreadyExistsException("email id already exists");

        Customer newCustomer = customerService.toCustomer(customerDto);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer savedCustomer = customerRepository.save(newCustomer);
        System.out.println("customer registered successfully.");
        String appUrl = request.getContextPath();
        sendActivationLinkMail(appUrl, savedCustomer, request.getLocale(), "Registration Confirmation");
        return "success";
    }

    public String createNewSeller(SellerRegistrationDto sellerRegistrationDto) {
        String message = sellerService.getUniquenessStatus(sellerRegistrationDto);
        if(!message.equals("unique"))
            return message;

        Seller seller = sellerService.toSeller(sellerRegistrationDto);
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));
        sellerRepository.save(seller);
        sellerService.sendAcknowledgementMail(seller.getEmail());
        return "success";
    }
}
