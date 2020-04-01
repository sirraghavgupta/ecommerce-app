package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.dtos.CustomerAdminApiDto;
import com.springbootcamp.ecommerceapp.dtos.CustomerRegistrationDto;
import com.springbootcamp.ecommerceapp.dtos.SellerAdminApiDto;
import com.springbootcamp.ecommerceapp.entities.Customer;
import com.springbootcamp.ecommerceapp.repos.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public Customer toCustomer(CustomerRegistrationDto cust){
        Customer customer = modelMapper.map(cust, Customer.class);
        return customer;
    }

    public CustomerAdminApiDto toCustomerAdminApiDto(Customer customer){
        CustomerAdminApiDto customerAdminApiDto = modelMapper.map(customer, CustomerAdminApiDto.class);
        customerAdminApiDto.setFullName(customer.getFirstName(), customer.getMiddleName(), customer.getLastName());
        return customerAdminApiDto;
    }

    public List<CustomerAdminApiDto> getAllCustomers(String offset, String size, String field) {
        Integer pageNo = Integer.parseInt(offset);
        Integer pageSize = Integer.parseInt(size);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(field).ascending());

        List<Customer> customers = customerRepository.findAll(pageable);

        List<CustomerAdminApiDto> customerAdminApiDtos = new ArrayList<>();

        customers.forEach((customer) -> customerAdminApiDtos.add(toCustomerAdminApiDto(customer)));
        return customerAdminApiDtos;
    }

    public CustomerAdminApiDto getCustomerByEmail(String email){
        Customer customer = customerRepository.findByEmail(email);
        CustomerAdminApiDto customerAdminApiDto = toCustomerAdminApiDto(customer);
        return customerAdminApiDto;
    }

}
