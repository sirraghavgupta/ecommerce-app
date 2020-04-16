package com.springbootcamp.ecommerceapp.services;

import com.google.common.collect.Sets;
import com.google.j2objc.annotations.AutoreleasePool;
import com.springbootcamp.ecommerceapp.dtos.ProductSellerDto;
import com.springbootcamp.ecommerceapp.dtos.ProductVariationSellerDto;
import com.springbootcamp.ecommerceapp.entities.*;
import com.springbootcamp.ecommerceapp.repos.*;
import com.springbootcamp.ecommerceapp.utils.*;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.xml.ws.Response;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SellerService sellerService;

    @Autowired
    EmailService emailService;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMetadataFieldRepository fieldRepository;

    @Autowired
    CategoryMetadataFieldValuesRepository valuesRepository;

    @Autowired
    ProductVariationRepository variationRepository;

    @Autowired
    ModelMapper modelMapper;

    public Product toProduct(ProductSellerDto productDto){
        if(productDto == null)
            return null;
        return modelMapper.map(productDto, Product.class);
    }

    public ProductSellerDto toProductSellerDto(Product product){
        if(product == null)
            return null;
        return modelMapper.map(product, ProductSellerDto.class);
    }

    public ProductVariation toProductVariation(ProductVariationSellerDto variationDto){
        if(variationDto==null)
            return null;
        return modelMapper.map(variationDto, ProductVariation.class);
    }

    public String validateNewProduct(String email, ProductSellerDto productDto){
        VO response;
        String message;

        Optional<Category> savedCategory = categoryRepository.findById(productDto.getCategoryId());
        if(!savedCategory.isPresent()){
            message = "Category does not exist.";
            return message;
        }
        Category category = savedCategory.get();
        if(!(category.getSubCategories() == null || category.getSubCategories().isEmpty())){
            message = "Category is not a leaf category.";
            return message;
        }

        Product savedProduct = productRepository.findByName(productDto.getName());
        if(savedProduct!=null){
            if(savedProduct.getCategory().getId().equals(productDto.getCategoryId())){
                if(savedProduct.getBrand().equalsIgnoreCase(productDto.getBrand())){
                    if(savedProduct.getSeller().getEmail().equalsIgnoreCase(email)){
                        message = "Product with similar details already exists.";
                        return message;
                    }
                }
            }
        }
        message = "success";
        return message;
    }

    public ResponseEntity<VO> saveNewProduct(String email, ProductSellerDto productDto) {
        VO response;
        String message = validateNewProduct(email, productDto);
        if(!message.equalsIgnoreCase("success")){
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<VO>(response, HttpStatus.BAD_REQUEST);
        }

        Category category = categoryRepository.findById(productDto.getCategoryId()).get();

        Product product = toProduct(productDto);
        Seller seller = sellerRepository.findByEmail(email);
        product.setCategory(category);
        product.setSeller(seller);
        productRepository.save(product);

        sendProductCreationMail(email, product);

        response = new ResponseVO<Product>(null, "Success", new Date());
        return new ResponseEntity<VO>(response, HttpStatus.CREATED);
    }

    private void sendProductCreationMail(String email, Product product) {
        String subject = "Product created";
        String content = "A product with following details has been created - \n" +
                "name - "+product.getName()+"\n" +
                "category - "+product.getCategory().getName()+"\n" +
                "brand - "+product.getBrand()+"\n" +
                "description - "+product.getDescription();
        emailService.sendEmail(email, subject, content);
    }

    public ResponseEntity<VO> saveNewProductVariation(String email, ProductVariationSellerDto variationDto) {

        VO response;
        String message = validateNewProductVariation(email, variationDto);

        if(!message.equalsIgnoreCase("success")){
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<VO>(response, HttpStatus.BAD_REQUEST);
        }

        // now we can save the product variation.
        ProductVariation newVariation = toProductVariation(variationDto);
        variationRepository.save(newVariation);

        message = "success";
        response = new ResponseVO<String>(null, message, new Date());
        return new ResponseEntity<VO>(response, HttpStatus.CREATED);
    }

    public String validateNewProductVariation(String email, ProductVariationSellerDto variationDto) {
        VO response;
        String message;

        Optional<Product> savedProduct = productRepository.findById(variationDto.getProductId());
        if(!savedProduct.isPresent()){
            message = "Parent product does not exist.";
            return message;
        }

        Product parentProduct = savedProduct.get();
        Category category = parentProduct.getCategory();
        Map<String, String> attributes = variationDto.getAttributes();


        if(parentProduct.getIsDeleted()){
            message = "Parent product does not exist.";
            return message;
        }
        if(!parentProduct.getIsActive()){
            message = "Parent product is inactive.";
            return message;
        }
        if(!parentProduct.getSeller().getEmail().equalsIgnoreCase(email)){
            message = "Parent product does not belong to you.";
            return message;
        }
        if(variationDto.getQuantityAvailable()<=0){
            message = "Quantity should be greater than 0.";
            return message;
        }
        if(variationDto.getPrice()<=0){
            message = "Price should be greater than 0";
            return message;
        }

        // check if all the fields are actually related to the product category.
        List<String> receivedFields = new ArrayList<>(attributes.keySet());
        List<String> actualFields = new ArrayList<>();
        valuesRepository.findAllFieldsOfCategoryById(category.getId())
                .forEach((e)->{
                    actualFields.add(e[0].toString());
                });

        if(receivedFields.size() < actualFields.size()){
            message = "Please provide all the fields related to the product category.";
            return message;
        }

        receivedFields.removeAll(actualFields);
        if(!receivedFields.isEmpty()){
            message = "Invalid fields found in the data.";
            return message;
        }

        // check validity of values of fields.
        List<String> receivedFieldsCopy = new ArrayList<>(attributes.keySet());

        for (String receivedField : receivedFieldsCopy) {

            CategoryMetadataField field = fieldRepository.findByName(receivedField);
            List<Object> savedValues = valuesRepository.findAllValuesOfCategoryField(category.getId(), field.getId());

            String values = savedValues.get(0).toString();
            Set<String> actualValueSet = StringToMapParser.toSetOfValues(values);

            String receivedValues = attributes.get(receivedField);
            Set<String> receivedValueSet = StringToMapParser.toSetOfValues(receivedValues);

            if(!Sets.difference(receivedValueSet, actualValueSet).isEmpty()){
                message = "Invalid values found for field "+receivedField;
                return message;
            }
        }
        return "success";
    }

    public ResponseEntity<VO> activateProductById(Long id) {
        VO response;
        String message;

        Optional<Product> savedProduct = productRepository.findById(id);
        if(!savedProduct.isPresent()){
            message = "Product with id - "+id+" not found.";
            response = new ErrorVO("Not Found", message, new Date());
            return new ResponseEntity<VO>(response, HttpStatus.BAD_REQUEST);
        }

        Product product = savedProduct.get();
        if(product.getIsActive()){
            message = "Product with id - "+id+" is already active.";
            response = new ResponseVO<String>(null, message, new Date());
            return new ResponseEntity<VO>(response, HttpStatus.BAD_REQUEST);
        }

        product.setIsActive(true);
        productRepository.save(product);
        String email = product.getSeller().getEmail();
        sendProductActivationMail(email, product);

        message = "success";
        response = new ResponseVO<String>(null, message, new Date());
        return new ResponseEntity<VO>(response, HttpStatus.OK);
    }

    private void sendProductActivationMail(String email, Product product) {
        String subject = "Product activated.";
        String content = "Your product "+product.getName()+" with id - "
                +product.getId()+" has been activated";
        emailService.sendEmail(email, subject, content);
    }
}
