package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.dtos.CustomerDto;
import com.springbootcamp.ecommerceapp.entities.Customer;
import com.springbootcamp.ecommerceapp.repos.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

//    public String validateNewCustomer(CustomerDto cust){
//        Customer savedCust = customerRepository.findByEmail(cust.getEmail());
//        if(savedCust != null)
//            return "email address already exists";
//
//        else if(){
//
//        }
//    }

    public Customer convertToCustomer(CustomerDto cust){
        Customer customer = modelMapper.map(cust, Customer.class);
        System.out.println("object converted");
        return customer;
    }


}
