package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.dtos.*;
import com.springbootcamp.ecommerceapp.entities.Address;
import com.springbootcamp.ecommerceapp.entities.Seller;
import com.springbootcamp.ecommerceapp.repos.SellerRepository;
import com.springbootcamp.ecommerceapp.utils.ResponseVO;
import com.springbootcamp.ecommerceapp.utils.BaseVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    PagingService pagingService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    AddressService addressService;

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

    public Seller toSeller(SellerViewProfileDto sellerDto){
        Seller seller =  modelMapper.map(sellerDto, Seller.class);
        AddressDto addressDto = sellerDto.getAddressDto();
        if(addressDto != null){
            Address address = addressService.toAddress(addressDto);
            seller.addAddress(address);
        }
        return seller;
    }

    public SellerViewProfileDto toSellerViewProfileDto(Seller seller){
        SellerViewProfileDto sellerViewProfileDto = modelMapper.map(seller, SellerViewProfileDto.class);
        AddressDto addressDto = addressService.toAddressDto(seller.getAddresses().stream().findFirst().get());
        sellerViewProfileDto.setAddressDto(addressDto);
        return sellerViewProfileDto;
    }

    public boolean isEmailUnique(String email){
        Seller seller = sellerRepository.findByEmailAndIsDeletedFalse(email);
        if(seller != null)
            return false;

        return true;
    }

    public boolean isGSTUnique(String GST){
        Seller seller = sellerRepository.findByGSTAndIsDeletedFalse(GST);
        if(seller != null)
            return false;

        return true;
    }
    public boolean isCompanyNameUnique(String name){
        Seller seller = sellerRepository.findByCompanyNameAndIsDeletedFalse(name);
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

    public List<SellerAdminApiDto> getAllSellers(String offset, String size, String sortByField, String order) {

        Pageable pageable = pagingService.getPageableObject(offset, size, sortByField, order);

        List<Seller> sellers = sellerRepository.findByIsDeletedFalse(pageable);
        List<SellerAdminApiDto> sellerAdminApiDtos = new ArrayList<>();

        sellers.forEach((seller)-> sellerAdminApiDtos.add(toSellerAdminApiDto(seller)));
        return sellerAdminApiDtos;
    }

    public SellerAdminApiDto getSellerByEmail(String email) {
        Seller seller = sellerRepository.findByEmailAndIsDeletedFalse(email);
        if(seller==null)
            return null;
        SellerAdminApiDto sellerAdminApiDto = toSellerAdminApiDto(seller);
        return sellerAdminApiDto;
    }

    public ResponseEntity<BaseVO> getUserProfile(String email) {
        Seller seller = sellerRepository.findByEmailAndIsDeletedFalse(email);
        BaseVO response;
        SellerViewProfileDto sellerViewProfileDto = toSellerViewProfileDto(seller);
        response = new ResponseVO<SellerViewProfileDto>(sellerViewProfileDto, null, new Date());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> updateUserProfile(String email, SellerViewProfileDto profileDto) {
        Seller savedSeller = sellerRepository.findByEmailAndIsDeletedFalse(email);

        if(profileDto.getFirstName() != null)
            savedSeller.setFirstName(profileDto.getFirstName());

        if(profileDto.getLastName() != null)
            savedSeller.setLastName(profileDto.getLastName());

        if(profileDto.getImage() != null)
            savedSeller.setImage(profileDto.getImage());

        if(profileDto.getIsActive() != null && !profileDto.getIsActive())
            savedSeller.setIsActive(profileDto.getIsActive());

        if(profileDto.getGST() != null)
            savedSeller.setGST(profileDto.getGST());

        if(profileDto.getCompanyContact() != null)
            savedSeller.setCompanyContact(profileDto.getCompanyContact());

        if(profileDto.getCompanyName() != null)
            savedSeller.setCompanyName(profileDto.getCompanyName());

        sellerRepository.save(savedSeller);

        BaseVO response = new ResponseVO<String>("null", "Your profile has been updated", new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }
}
