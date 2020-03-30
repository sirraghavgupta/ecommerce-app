package com.springbootcamp.ecommerceapp.events;

import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<ActivationEmailFireEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(ActivationEmailFireEvent event) {
        this.confirmRegistration(event);
    }

    @Async
    private void sendMail(SimpleMailMessage email){
        mailSender.send(email);
    }

    private void confirmRegistration(ActivationEmailFireEvent event) {

        // create and save the token
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(token, user);

        // prepare the email contents
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/activate/customer?token=" + token;
        String message = messages.getMessage("message.regSucc", null, event.getLocale());
//        String message = "please activate ur account";

        System.out.println(confirmationUrl);

        // populate the email and send
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        sendMail(email);
    }

}
