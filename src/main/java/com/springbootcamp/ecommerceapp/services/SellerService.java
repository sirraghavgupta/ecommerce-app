package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.dtos.SellerAdminApiDto;
import com.springbootcamp.ecommerceapp.dtos.SellerRegistrationDto;
import com.springbootcamp.ecommerceapp.entities.Seller;
import com.springbootcamp.ecommerceapp.repos.SellerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    public Seller toSeller(SellerRegistrationDto sellerDto){
        Seller seller = modelMapper.map(sellerDto, Seller.class);
        return seller;
    }

    public SellerAdminApiDto toSellerAdminApiDto(Seller seller){
        SellerAdminApiDto sellerAdminApiDto = modelMapper.map(seller, SellerAdminApiDto.class);
        sellerAdminApiDto.setFullName(seller.getFirstName(), seller.getMiddleName(), seller.getLastName());
        return sellerAdminApiDto;
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

    public String getUniquenessStatus(SellerRegistrationDto sellerRegistrationDto){
        if(!isEmailUnique(sellerRegistrationDto.getEmail()))
            return "Email id already exists";

        else if(!isGSTUnique(sellerRegistrationDto.getGST()))
            return "GST already exists";

        else if(!isCompanyNameUnique(sellerRegistrationDto.getCompanyName()))
            return "Company Name already exists";

        return "unique";
    }

    public void sendAcknowledgementMail(String emailAddress){

        String subject = "Registration Confirmation";
        String message = "your account has been created and is pending for approval by our team. ";
        emailService.sendEmail(emailAddress, subject, message);
    }

    public List<SellerAdminApiDto> getAllSellers(String offset, String size, String field) {
        Integer pageNo = Integer.parseInt(offset);
        Integer pageSize = Integer.parseInt(size);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(field).ascending());

        List<Seller> sellers = sellerRepository.findAll(pageable);
        List<SellerAdminApiDto> sellerAdminApiDtos = new ArrayList<>();

        sellers.forEach((seller)-> sellerAdminApiDtos.add(toSellerAdminApiDto(seller)));
        return sellerAdminApiDtos;
    }

    public SellerAdminApiDto getSellerByEmail(String email) {
        Seller seller = sellerRepository.findByEmail(email);
        SellerAdminApiDto sellerAdminApiDto = toSellerAdminApiDto(seller);
        return sellerAdminApiDto;
    }
}
