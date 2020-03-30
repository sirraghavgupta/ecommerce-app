package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.dtos.CustomerDto;
import com.springbootcamp.ecommerceapp.dtos.SellerDto;
import com.springbootcamp.ecommerceapp.entities.Customer;
import com.springbootcamp.ecommerceapp.entities.Seller;
import com.springbootcamp.ecommerceapp.repos.CustomerRepository;
import com.springbootcamp.ecommerceapp.repos.SellerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender mailSender;

    public Seller convertToSeller(SellerDto sellr){
        Seller seller = modelMapper.map(sellr, Seller.class);
        System.out.println("object converted");
        return seller;
    }

    public boolean isEmailUnique(String email){
        Seller seller = sellerRepository.findByEmail(email);
        if(seller != null)
            return false;

        return true;
    }

    public boolean isGSTUnique(String GST){
        Seller seller = sellerRepository.findByGST(GST);
        if(seller != null)
            return false;

        return true;
    }
    public boolean isCompanyNameUnique(String name){
        Seller seller = sellerRepository.findByCompanyName(name);
        if(seller != null)
            return false;

        return true;
    }

    public String getUniquenessStatus(SellerDto sellerDto){
        if(!isEmailUnique(sellerDto.getEmail()))
            return "Email id already exists";

        else if(!isGSTUnique(sellerDto.getGST()))
            return "GST already exists";

        else if(!isCompanyNameUnique(sellerDto.getCompanyName()))
            return "Company Name already exists";

        return "unique";
    }


    @Async
    public void sendAcknowledgementMail(String emailAddress){

        String subject = "Registration Confirmation";
        String message = "your account has been created and is pending for approval by our team. ";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(emailAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

}
