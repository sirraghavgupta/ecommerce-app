package com.springbootcamp.ecommerceapp.controllers;

import com.springbootcamp.ecommerceapp.dtos.ProductSellerDto;
import com.springbootcamp.ecommerceapp.dtos.ProductUpdateDto;
import com.springbootcamp.ecommerceapp.dtos.ProductVariationSellerDto;
import com.springbootcamp.ecommerceapp.dtos.ProductVariationUpdateDto;
import com.springbootcamp.ecommerceapp.services.ProductService;
import com.springbootcamp.ecommerceapp.utils.BaseVO;
import org.codehaus.jackson.map.Serializers;
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
    public ResponseEntity<BaseVO> createProduct(@RequestBody ProductSellerDto productSellerDto, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return productService.saveNewProduct(username, productSellerDto);
    }

    @GetMapping("/seller/product/{id}")
    public ResponseEntity<BaseVO> getProductForSeller(@PathVariable Long id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        return productService.getProductByIdForSeller(id, email);
    }

    @DeleteMapping("/seller/product/{id}")
    public ResponseEntity<BaseVO> deleteProductById(@PathVariable Long id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        return productService.deleteProductById(id, email);
    }



    @PatchMapping("/seller/product/{productId}")
    public ResponseEntity<BaseVO> updateProductById(@PathVariable Long productId, @RequestBody ProductUpdateDto productDto, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        return productService.updateProductByProductId(productId, email, productDto);
    }


    @GetMapping("/seller/products")
    public ResponseEntity<BaseVO> getAllProductsForSeller(@RequestParam(defaultValue = "0") String offset,
                                        @RequestParam(defaultValue = "10") String size,
                                        @RequestParam(defaultValue = "id") String sortByField,
                                        @RequestParam(defaultValue = "ascending") String order,
                                        @RequestParam(required = false) Long categoryId,
                                        @RequestParam(required = false) String brand){
        return productService.getAllProductsForSeller(offset, size, sortByField, order, categoryId, brand);
    }

    @PostMapping("/seller/product-variations")
    public ResponseEntity<BaseVO> createProductVariation(@RequestBody ProductVariationSellerDto variationDto, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return productService.saveNewProductVariation(username, variationDto);
    }

    @PatchMapping("/seller/product-variation/{variationId}")
    public ResponseEntity<BaseVO> updateProductVariationById(
                                             @PathVariable Long variationId,
                                             @RequestBody ProductVariationUpdateDto variationDto,
                                             HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        return productService.updateProductVariationById(variationId, email, variationDto);
    }

    @GetMapping("/seller/product-variations/{productId}")
    public ResponseEntity<BaseVO> getAllProductVariationsByProductIdForSeller(@PathVariable Long productId,
                                                                              @RequestParam(defaultValue = "0") String offset,
                                                                              @RequestParam(defaultValue = "10") String size,
                                                                              @RequestParam(defaultValue = "id") String sortByField,
                                                                              @RequestParam(defaultValue = "ascending") String order,
                                                                              HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        System.out.println("called up #############============###########");
        return productService.getAllProductVariationsByProductIdForSeller(email, productId, offset, size, sortByField, order);
    }

    @GetMapping("/seller/product-variation/{id}")
    public ResponseEntity<BaseVO> getProductVariationByIdForSeller(@PathVariable Long id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String email = principal.getName();

        return productService.getProductVariationByIdForSeller(email, id);
    }


    @PutMapping("/product/activate/{id}")
    public ResponseEntity<BaseVO> activateProduct(@PathVariable Long id){
        return productService.activateProductById(id);
    }

    @PutMapping("/product/deactivate/{id}")
    public ResponseEntity<BaseVO> deactivateProduct(@PathVariable Long id){
        return productService.deactivateProductById(id);
    }


    @GetMapping("/customer/product/{productId}")
    public ResponseEntity<BaseVO> getProductForCustomer(@PathVariable Long productId){
        return productService.getProductByIdForCustomer(productId);
    }

    @GetMapping("/customer/products/{categoryId}")
    public ResponseEntity<BaseVO> getAllProductsByCategoryIdForCustomer(@PathVariable Long categoryId,
                                                            @RequestParam(defaultValue = "0") String offset,
                                                            @RequestParam(defaultValue = "10") String size,
                                                            @RequestParam(defaultValue = "id") String sortByField,
                                                            @RequestParam(defaultValue = "ascending") String order){
        return productService.getAllProductsByCategoryIdForCustomer(categoryId, offset, size, sortByField, order);
    }

    @GetMapping("/customer/similar-products/{productId}")
    public ResponseEntity<BaseVO> getSimilarProductsByProductIdForCustomer(@PathVariable Long productId,
                                                                        @RequestParam(defaultValue = "0") String offset,
                                                                        @RequestParam(defaultValue = "10") String size,
                                                                        @RequestParam(defaultValue = "id") String sortByField,
                                                                        @RequestParam(defaultValue = "ascending") String order){

        return productService.getAllSimilarProductsByProductId(productId, offset, size, sortByField, order);
    }

}
