package com.springbootcamp.ecommerceapp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Map;

public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> productVariationInfo) {

        String productVariationInfoJson = null;
        try {
            productVariationInfoJson = objectMapper.writeValueAsString(productVariationInfo);
        } catch (final JsonProcessingException e) {
            System.out.println(e);
        }

        return productVariationInfoJson;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String productVariationInfoJSON) {

        Map<String, Object> productVariationInfo = null;
        try {
            productVariationInfo = objectMapper.readValue(productVariationInfoJSON, Map.class);
        } catch (final IOException e) {
            System.out.println(e);
        }

        return productVariationInfo;
    }

}
