package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.repos.ProductVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductVariationService {

    @Autowired
    ProductVariationRepository variationRepository;

    public void deleteVariationById(Long v_id){
        variationRepository.deleteVariationById(v_id);
    }

    public void deleteVariationByProductId(Long p_id){
        variationRepository.deleteByProductId(p_id);
    }
}
