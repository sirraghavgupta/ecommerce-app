package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.entities.Product;
import com.springbootcamp.ecommerceapp.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
