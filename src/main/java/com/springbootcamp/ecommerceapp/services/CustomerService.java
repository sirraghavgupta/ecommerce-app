package com.springbootcamp.ecommerceapp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.springbootcamp.ecommerceapp.dtos.*;
import com.springbootcamp.ecommerceapp.entities.Address;
import com.springbootcamp.ecommerceapp.entities.Customer;
import com.springbootcamp.ecommerceapp.entities.User;
import com.springbootcamp.ecommerceapp.repos.AddressRepository;
import com.springbootcamp.ecommerceapp.repos.CustomerRepository;
import com.springbootcamp.ecommerceapp.utils.ErrorVO;
import com.springbootcamp.ecommerceapp.utils.ResponseVO;
import com.springbootcamp.ecommerceapp.utils.VO;
import org.hibernate.type.CustomType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    AddressService addressService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    ObjectMapper objectMapper = new ObjectMapper();


    public Customer toCustomer(CustomerRegistrationDto customerDto){
        System.out.println("called to customer");
        return modelMapper.map(customerDto, Customer.class);
    }

    public CustomerAdminApiDto toCustomerAdminApiDto(Customer customer){
        CustomerAdminApiDto customerAdminApiDto = modelMapper.map(customer, CustomerAdminApiDto.class);
        customerAdminApiDto.setFullName(customer.getFirstName(), customer.getMiddleName(), customer.getLastName());
        return customerAdminApiDto;
    }

    public Customer toCustomer(CustomerViewProfileDto customerDto){
        return modelMapper.map(customerDto, Customer.class);
    }

    public CustomerViewProfileDto toCustomerViewProfileDto(Customer customer){
        CustomerViewProfileDto customerViewProfileDto = modelMapper.map(customer, CustomerViewProfileDto.class);
        return customerViewProfileDto;
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
        if(customer==null)
            return null;
        CustomerAdminApiDto customerAdminApiDto = toCustomerAdminApiDto(customer);
        return customerAdminApiDto;
    }

    public ResponseEntity<VO> getUserProfile(String email) {
        Customer customer = customerRepository.findByEmail(email);
        VO response;
        CustomerViewProfileDto customerViewProfileDto = toCustomerViewProfileDto(customer);
        response = new ResponseVO<CustomerViewProfileDto>(customerViewProfileDto, null, new Date());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<VO> getCustomerAddresses(String email) {
        Customer customer = customerRepository.findByEmail(email);
        VO response;
        Set<AddressDto> addressDtos = new HashSet<>();
        Set<Address> addresses = customer.getAddresses();

        addresses.forEach(
                (a)->addressDtos.add(addressService.toAddressDto(a))
        );
        response = new ResponseVO<Set>(addressDtos, null, new Date());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<VO> addNewAddress(String email, AddressDto addressDto) {
        VO response;
        Customer customer = customerRepository.findByEmail(email);
        Address newAddress = addressService.toAddress(addressDto);
        customer.addAddress(newAddress);
        customerRepository.save(customer);
        response = new ResponseVO<String>(null, "success", new Date());
        return new ResponseEntity<VO>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<VO> deleteAddress(String email, Long id) {
        VO response;
        Optional<Address> optAddress = addressRepository.findById(id);
        if(!optAddress.isPresent()){
            response = new ErrorVO("Not found", "No address found with this id", new Date());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Address savedAddress = optAddress.get();
        if(savedAddress.getUser().getEmail().equals(email)){
            addressRepository.deleteAddressById(id);
            response = new ResponseVO<String>("null", "Success", new Date());
            return new ResponseEntity<VO>(response, HttpStatus.OK);
        }
        response = new ErrorVO("Invalid Operation", "This address doesn't belong to this user.", new Date());
        return new ResponseEntity<VO>(response, HttpStatus.CONFLICT);
    }

    public ResponseEntity<VO> updateUserProfile(String email, CustomerViewProfileDto profileDto) {
        Customer savedCustomer = customerRepository.findByEmail(email);
        if(profileDto.getFirstName() != null)
            savedCustomer.setFirstName(profileDto.getFirstName());

        if(profileDto.getLastName() != null)
            savedCustomer.setLastName(profileDto.getLastName());

        if(profileDto.getImage() != null)
            savedCustomer.setImage(profileDto.getImage());

        if(profileDto.getContact() != null)
            savedCustomer.setContact(profileDto.getContact());

        if(profileDto.getIsActive() != null && !profileDto.getIsActive())
            savedCustomer.setActive(profileDto.getIsActive());

        customerRepository.save(savedCustomer);

        VO response = new ResponseVO<String>("null", "Your profile has been updated.", new Date());
        return new ResponseEntity<VO>(response, HttpStatus.OK);
    }


}