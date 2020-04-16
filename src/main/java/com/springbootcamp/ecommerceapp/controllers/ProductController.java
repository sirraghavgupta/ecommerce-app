package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.ProductSellerDto;
import com.springbootcamp.ecommerceapp.dtos.ProductVariationSellerDto;
import com.springbootcamp.ecommerceapp.services.ProductService;
import com.springbootcamp.ecommerceapp.utils.VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/seller/products")
    public ResponseEntity<VO> createProduct(@RequestBody ProductSellerDto productSellerDto, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return productService.saveNewProduct(username, productSellerDto);
    }

    @PostMapping("/seller/product-variation")
    public ResponseEntity<VO> createProductVariation(@RequestBody ProductVariationSellerDto variationDto, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return productService.saveNewProductVariation(username, variationDto);
    }


    @PutMapping("/product/activate/{id}")
    public ResponseEntity<VO> activateProduct(@PathVariable Long id){
        return productService.activateProductById(id);
    }

}
