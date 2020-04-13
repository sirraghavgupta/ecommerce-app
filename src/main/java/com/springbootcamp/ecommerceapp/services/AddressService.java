package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.dtos.AddressDto;
import com.springbootcamp.ecommerceapp.dtos.CustomerAdminApiDto;
import com.springbootcamp.ecommerceapp.dtos.CustomerRegistrationDto;
import com.springbootcamp.ecommerceapp.entities.Address;
import com.springbootcamp.ecommerceapp.entities.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private ModelMapper modelMapper;

    public Address toAddress(AddressDto addressDto){
        if(addressDto != null)
             return modelMapper.map(addressDto, Address.class);
        return null;
    }

    public AddressDto toAddressDto(Address address){
        if(address != null)
            return modelMapper.map(address, AddressDto.class);
        return null;
    }

}
