package com.springbootcamp.ecommerceapp.services;

import com.google.common.collect.Sets;
import com.springbootcamp.ecommerceapp.dtos.*;
import com.springbootcamp.ecommerceapp.entities.*;
import com.springbootcamp.ecommerceapp.repos.*;
import com.springbootcamp.ecommerceapp.utils.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PagingService pagingService;

    @Autowired
    EmailService emailService;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductVariationService variationService;

    @Autowired
    CategoryMetadataFieldRepository fieldRepository;

    @Autowired
    CategoryMetadataFieldValuesRepository valuesRepository;

    @Autowired
    ProductVariationRepository variationRepository;

    @Autowired
    ProductReviewRepository reviewRepository;

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

    public ProductVariationSellerDto toProductVariationSellerDto(ProductVariation variation){
        if(variation==null)
            return null;
        return modelMapper.map(variation, ProductVariationSellerDto.class);
    }

    public String validateNewProduct(String email, ProductSellerDto productDto){
        BaseVO response;
        String message;

        Optional<Category> savedCategory = categoryRepository.findByIdAndIsDeletedFalse(productDto.getCategoryId());
        if(!savedCategory.isPresent()){
            message = "Category does not exist.";
            return message;
        }
        Category category = savedCategory.get();
        if(!(category.getSubCategories() == null || category.getSubCategories().isEmpty())){
            message = "Category is not a leaf category.";
            return message;
        }

        Product savedProduct = productRepository.findByNameAndIsDeletedFalse(productDto.getName());
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

    public ResponseEntity<BaseVO> saveNewProduct(String email, ProductSellerDto productDto) {
        BaseVO response;
        String message = validateNewProduct(email, productDto);
        if(!message.equalsIgnoreCase("success")){
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        Category category = categoryRepository.findByIdAndIsDeletedFalse(productDto.getCategoryId()).get();

        Product product = toProduct(productDto);
        Seller seller = sellerRepository.findByEmailAndIsDeletedFalse(email);
        product.setCategory(category);
        product.setSeller(seller);
        productRepository.save(product);

        sendProductCreationMail(email, product);

        response = new ResponseVO<Product>(null, "Success", new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.CREATED);
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

    public ResponseEntity<BaseVO> saveNewProductVariation(String email, ProductVariationSellerDto variationDto) {

        BaseVO response;
        String message = validateNewProductVariation(email, variationDto);

        if(!message.equalsIgnoreCase("success")){
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        // now we can save the product variation.
        ProductVariation newVariation = toProductVariation(variationDto);
        variationRepository.save(newVariation);

        message = "success";
        response = new ResponseVO<String>(null, message, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.CREATED);
    }

    public String validateNewProductVariation(String email, ProductVariationSellerDto variationDto) {
        BaseVO response;
        String message;

        Optional<Product> savedProduct = productRepository.findByIdAndIsDeletedFalse(variationDto.getProductId());
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

            CategoryMetadataField field = fieldRepository.findByNameAndIsDeletedFalse(receivedField);
            List<Object> savedValues = valuesRepository.findAllValuesOfCategoryField(category.getId(), field.getId());

            String values = savedValues.get(0).toString();
            Set<String> actualValueSet = StringToSetParser.toSetOfValues(values);

            String receivedValues = attributes.get(receivedField);
            Set<String> receivedValueSet = StringToSetParser.toSetOfValues(receivedValues);

            if(!Sets.difference(receivedValueSet, actualValueSet).isEmpty()){
                message = "Invalid value found for field "+receivedField;
                return message;
            }
        }
        return "success";
    }

    public ResponseEntity<BaseVO> activateProductById(Long id) {
        BaseVO response;
        String message;

        Optional<Product> savedProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if(!savedProduct.isPresent()){
            message = "Product with id - "+id+" not found.";
            response = new ErrorVO("Not Found", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        Product product = savedProduct.get();
        if(product.getIsActive()){
            message = "Product with id - "+id+" is already active.";
            response = new ResponseVO<String>(null, message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        product.setIsActive(true);
        productRepository.save(product);
        String email = product.getSeller().getEmail();
        sendProductActivationDeactivationMail(email, product, true);

        message = "success";
        response = new ResponseVO<String>(null, message, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    private void sendProductActivationDeactivationMail(String email, Product product, boolean activation) {
        String subject;
        if(activation)
            subject = "Product activated";
        else
            subject = "Product deactivated";

        String content = "Your product "+product.getName()+" with id - "
                +product.getId()+" has been activated";
        emailService.sendEmail(email, subject, content);
    }

    public ResponseEntity<BaseVO> deactivateProductById(Long id) {
        BaseVO response;
        String message;

        Optional<Product> savedProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if(!savedProduct.isPresent()){
            message = "Product with id - "+id+" not found.";
            response = new ErrorVO("Not Found", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        Product product = savedProduct.get();
        if(!product.getIsActive()){
            message = "Product with id - "+id+" is already deactivated.";
            response = new ResponseVO<String>(null, message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        product.setIsActive(false);
        productRepository.save(product);
        String email = product.getSeller().getEmail();
        sendProductActivationDeactivationMail(email, product, false);

        message = "success";
        response = new ResponseVO<String>(null, message, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getProductByIdForSeller(Long id, String email) {
        BaseVO response;
        String message;

        Optional<Product> savedProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if(!savedProduct.isPresent()){
            response = new ErrorVO("Validation failed", "Product with id "+id+ " not found", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }
        Product product = savedProduct.get();
        if(!product.getSeller().getEmail().equalsIgnoreCase(email)){
            message = "Product with id "+id+ " does not belong to you.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }
        if(product.getIsDeleted()){
            message = "Product does not exist.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }


        ProductSellerDto productSellerDto = toProductSellerDto(product);
        productSellerDto.setCategoryDto(categoryService.toCategoryDto(product.getCategory()));

        response = new ResponseVO<ProductSellerDto>(productSellerDto, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getAllProductsForSeller(String offset, String size, String sortByField, String order, Long categoryId, String brand) {
        BaseVO response;

        Pageable pageable = pagingService.getPageableObject(offset, size, sortByField, order);

        List<Product> products;
        if(categoryId!=null && brand!=null){
            products = productRepository.findByBrandAndCategoryIdAndIsDeletedFalse(brand, categoryId, pageable);
        }
        else if(categoryId!=null){
            products = productRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);
        }
        else if(brand != null){
            products = productRepository.findByBrandAndIsDeletedFalse(brand, pageable);
        }
        else{
            products = productRepository.findByIsDeletedFalse(pageable);
        }

        List<ProductSellerDto> productDtos = new ArrayList<>();
        products.forEach(product -> {
            ProductSellerDto dto = toProductSellerDto(product);
            dto.setCategoryDto(categoryService.toCategoryDto(product.getCategory()));
            productDtos.add(dto);
        });

        response = new ResponseVO<List>(productDtos, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getProductVariationByIdForSeller(String email, Long id) {
        BaseVO response;
        String message;

        Optional<ProductVariation> savedVariation = variationRepository.findByIdAndIsDeletedFalse(id);
        if(!savedVariation.isPresent()){
            response = new ErrorVO("Validation failed", "Product variation with id "+id+ " not found", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }
        ProductVariation variation = savedVariation.get();
        if(!variation.getProduct().getSeller().getEmail().equalsIgnoreCase(email)){
            message = "Product variation with id "+id+ " does not belong to you.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }
        if(variation.isDeleted()){
            message = "Product Variation does not exist.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        ProductVariationSellerDto variationDto = toProductVariationSellerDto(variation);
        ProductSellerDto productDto = toProductSellerDto(variation.getProduct());
        productDto.setCategoryDto(categoryService.toCategoryDto(variation.getProduct().getCategory()));
        variationDto.setProductDto(productDto);

        response = new ResponseVO<ProductVariationSellerDto>(variationDto, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getAllProductVariationsByProductIdForSeller(String email, Long id, String offset, String size, String sortByField, String order) {
        BaseVO response;
        String message;

        Optional<Product> savedProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if(!savedProduct.isPresent()){
            response = new ErrorVO("Validation failed", "Product with id "+id+ " not found", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }
        Product product = savedProduct.get();
        if(!product.getSeller().getEmail().equalsIgnoreCase(email)){
            message = "Product with id "+id+ " does not belong to you.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }
        if(product.getIsDeleted()){
            message = "Product does not exist.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        Pageable pageable = pagingService.getPageableObject(offset, size, sortByField, order);

        List<ProductVariation> variations;
        variations = variationRepository.findByProductIdAndIsDeletedFalse(id, pageable);

        List<ProductVariationSellerDto> variationDtos = new ArrayList<>();
        variations.forEach(variation -> {
            ProductVariationSellerDto variationDto = toProductVariationSellerDto(variation);
            ProductSellerDto productDto = toProductSellerDto(variation.getProduct());
            productDto.setCategoryDto(categoryService.toCategoryDto(variation.getProduct().getCategory()));
            variationDto.setProductDto(productDto);
            variationDtos.add(variationDto);
        });

        response = new ResponseVO<List>(variationDtos, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> deleteProductById(Long id, String email) {
        BaseVO response;
        String message;

        Optional<Product> savedProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if(!savedProduct.isPresent()){
            response = new ErrorVO("Validation failed", "Product with id "+id+ " not found", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }
        Product product = savedProduct.get();
        if(!product.getSeller().getEmail().equalsIgnoreCase(email)){
            message = "Product with id "+id+ " does not belong to you.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        deleteProductById(id);

        response = new ResponseVO<String>(null,"success", new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> validateProductUpdate(Long id, String email, ProductUpdateDto productDto){
        BaseVO response;
        String message;

        Optional<Product> savedProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if(!savedProduct.isPresent()){
            message = "Product with id "+id+ " not found";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }
        Product product = savedProduct.get();
        if(!product.getSeller().getEmail().equalsIgnoreCase(email)){
            message = "Product with id "+id+ " does not belong to you.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }
        if(product.getIsDeleted()){
            message = "Product does not exist.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        if(productDto.getName() != null){
            Product duplicateProduct = productRepository.findByNameAndIsDeletedFalse(productDto.getName());
            if(duplicateProduct!=null){
                if(duplicateProduct.getCategory().getId().equals(product.getCategory().getId())){
                    if(duplicateProduct.getBrand().equalsIgnoreCase(product.getBrand())){
                        if(duplicateProduct.getSeller().getEmail().equalsIgnoreCase(email)){
                            message = "Product with similar details already exists.";
                            response = new ErrorVO("Validation failed", message, new Date());
                            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
                        }
                    }
                }
            }
        }
        return null;
    }

    public ResponseEntity<BaseVO> updateProductByProductId(Long id, String email, ProductUpdateDto productDto) {
        BaseVO response;
        String message;

        ResponseEntity<BaseVO> validationResult = validateProductUpdate(id, email, productDto);
        if(validationResult != null){
            return validationResult;
        }

        Product product = productRepository.findByIdAndIsDeletedFalse(id).get();
        applyProductUpdateDtoToProduct(product, productDto);
        productRepository.save(product);

        response = new ResponseVO<String>(null, "success", new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public void applyProductUpdateDtoToProduct(Product product, ProductUpdateDto productDto) {

        if(productDto.getName() != null)
            product.setName(productDto.getName());

        if(productDto.getDescription() != null)
            product.setDescription(productDto.getDescription());

        if(productDto.getIsReturnable() != null)
            product.setIsReturnable(productDto.getIsReturnable());

        if(productDto.getIsCancelleable() != null)
            product.setIsCancelleable(productDto.getIsCancelleable());

    }

    public ResponseEntity<BaseVO> validateProductVariationUpdate(Long id, String email, ProductVariationUpdateDto variationDto) {
        BaseVO response;
        String message;

        Optional<ProductVariation> savedVariation = variationRepository.findByIdAndIsDeletedFalse(id);
        if(!savedVariation.isPresent()){
            message = "Product variation with id "+id+ " not found";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        ProductVariation variation = savedVariation.get();

        if(variation.isDeleted()){
            message = "Product variation does not exist.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }
        if(!variation.getProduct().getIsActive()){
            message = "Parent product is inactive.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }
        if(!variation.getProduct().getSeller().getEmail().equalsIgnoreCase(email)){
            message = "Product variation does not belong to you.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);

        }
        if( variationDto.getQuantityAvailable()!=null && variationDto.getQuantityAvailable()<=0){
            message = "Quantity should be greater than 0.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }
        if( variationDto.getPrice()!=null && variationDto.getPrice()<=0){
            message = "Price should be greater than 0";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        // check if all the fields are actually related to the product category.
        Map<String, String> attributes = variationDto.getAttributes();
        if(attributes!=null){
            Category category = variation.getProduct().getCategory();
            List<String> receivedFields = new ArrayList<>(attributes.keySet());
            List<String> actualFields = new ArrayList<>();
            valuesRepository.findAllFieldsOfCategoryById(category.getId())
                    .forEach((e)->{
                        actualFields.add(e[0].toString());
                    });

            receivedFields.removeAll(actualFields);
            if(!receivedFields.isEmpty()){
                message = "Invalid fields found in the data.";
                response = new ErrorVO("Validation failed", message, new Date());
                return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
            }

            // check validity of values of fields.
            List<String> receivedFieldsCopy = new ArrayList<>(attributes.keySet());

            for (String receivedField : receivedFieldsCopy) {

                CategoryMetadataField field = fieldRepository.findByNameAndIsDeletedFalse(receivedField);
                List<Object> savedValues = valuesRepository.findAllValuesOfCategoryField(category.getId(), field.getId());

                String values = savedValues.get(0).toString();
                Set<String> actualValueSet = StringToSetParser.toSetOfValues(values);

                String receivedValues = attributes.get(receivedField);
                Set<String> receivedValueSet = StringToSetParser.toSetOfValues(receivedValues);

                if(!Sets.difference(receivedValueSet, actualValueSet).isEmpty()){
                    message = "Invalid value found for field "+receivedField;
                    response = new ErrorVO("Validation failed", message, new Date());
                    return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
                }
            }

        }
        return null;
    }

    public ResponseEntity<BaseVO> updateProductVariationById(Long id, String email, ProductVariationUpdateDto variationDto) {
        BaseVO response;
        String message;

        ResponseEntity<BaseVO> validationResponse = validateProductVariationUpdate(id, email, variationDto);
        if(validationResponse!=null)
            return validationResponse;

        ProductVariation variation = variationRepository.findByIdAndIsDeletedFalse(id).get();

        // now we can save the product variation.
        applyProductVariationUpdateDtoToProductVariation(variation, variationDto);
        variationRepository.save(variation);

        message = "success";
        response = new ResponseVO<String>(null, message, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);

    }

    private void applyProductVariationUpdateDtoToProductVariation(ProductVariation variation, ProductVariationUpdateDto variationDto) {

        if(variationDto.getQuantityAvailable()!=null)
            variation.setQuantityAvailable(variationDto.getQuantityAvailable());

        if(variationDto.getPrice() != null)
            variation.setPrice(variationDto.getPrice());

        if(variationDto.getIsActive() != null)
            variation.setActive(variationDto.getIsActive());

        if(variationDto.getAttributes() != null){
            Map<String, String> newAttributes = variationDto.getAttributes();
            if(!newAttributes.isEmpty()){
                Map<String, String> oldAttributes = variation.getProductAttributes();

                for(String key : newAttributes.keySet()){
                    String newValue = newAttributes.get(key);
                    oldAttributes.put(key, newValue);
                }
            }
        }
    }

    public ResponseEntity<BaseVO> getProductByIdForCustomer(Long id){
        BaseVO response;
        String message;

        Optional<Product> savedProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if(!savedProduct.isPresent()){
            message = "Product with id "+id+ " not found";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }
        Product product = savedProduct.get();

        if(product.getIsDeleted()){
            message = "Product with id "+id+ " not found";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }
        if(!product.getIsActive()){
            message = "Product is inactive.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }
        if(product.getVariations()==null || product.getVariations().isEmpty()){
            message = "No variations available for this product";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        // now we are ready to return the product/
        ProductCustomerViewDto productCustomerViewDto = getProductCustomerViewDto(product);

        response = new ResponseVO<ProductCustomerViewDto>(productCustomerViewDto, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getAllProductsByCategoryIdForCustomer(Long categoryId, String offset, String size, String sortByField, String order) {

        BaseVO response;
        String message;

        Optional<Category> savedCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId);
        if(!savedCategory.isPresent()){
            message = "Category with id - " + categoryId + " does not exist.";
            response = new ErrorVO("Not found.", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        Category category= savedCategory.get();
        Pageable pageable = pagingService.getPageableObject(offset, size, sortByField, order);
        Set<ProductCustomerViewDto> productCustomerViewDtos;

        productCustomerViewDtos = getAllProductCustomerViewDtosByCategory(categoryId, pageable);

        response = new ResponseVO<Set>(productCustomerViewDtos, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);

    }

    public ProductCustomerViewDto getProductCustomerViewDto(Product product){
        ProductSellerDto productDto = toProductSellerDto(product);
        productDto.setCategoryDto(categoryService.toCategoryDto(product.getCategory()));

        Set<ProductVariationSellerDto> variationDtos = new HashSet<>();
        product.getVariations().forEach((variation -> {
            ProductVariationSellerDto variationDto = toProductVariationSellerDto(variation);
            variationDto.setProductDto(null);
            variationDtos.add(variationDto);
        }));

        ProductCustomerViewDto productCustomerViewDto = new ProductCustomerViewDto();

        productCustomerViewDto.setProductDto(productDto);
        productCustomerViewDto.setVariations(variationDtos);

        return productCustomerViewDto;
    }

    public Set<ProductCustomerViewDto> getAllProductCustomerViewDtosByCategory(Long categoryId, Pageable pageable){
        Set<ProductCustomerViewDto> productCustomerViewDtos = new LinkedHashSet<>();

        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).get();

        if(category.getSubCategories() == null || category.getSubCategories().isEmpty()){
            List<Product> products = productRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);
            for (Product product : products) {
                productCustomerViewDtos.add(getProductCustomerViewDto(product));
            }
        }
        else{
            for(Category child : category.getSubCategories()){
                productCustomerViewDtos.addAll(getAllProductCustomerViewDtosByCategory(child.getId(), pageable));
            }
        }
        return productCustomerViewDtos;
    }

    public ResponseEntity<BaseVO> getAllSimilarProductsByProductId(Long id, String offset, String size, String sortByField, String order) {
        BaseVO response;
        String message;

        Optional<Product> savedProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if(!savedProduct.isPresent()){
            message = "Product with id "+id+ " not found";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        Product product = savedProduct.get();

        if(product.getIsDeleted()){
            message = "Product with id "+id+ " not found";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }
        if(!product.getIsActive()){
            message = "Product is inactive.";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        Category category = product.getCategory();

        Pageable pageable = pagingService.getPageableObject(offset, size, sortByField, order);

        Set<ProductCustomerViewDto> similarProducts = getAllProductCustomerViewDtosByCategory(category.getId(), pageable);
        similarProducts.remove(getProductCustomerViewDto(product));

        if(similarProducts.isEmpty()){
            message = "No similar products found.";
            response = new ResponseVO<Set>(similarProducts, message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
        }

        response = new ResponseVO<Set>(similarProducts, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getProductByIdForAdmin(Long id){
        BaseVO response;
        String message;

        Optional<Product> savedProduct = productRepository.findByIdAndIsDeletedFalse(id);
        if(!savedProduct.isPresent()){
            message = "Product with id "+id+ " not found";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        Product product = savedProduct.get();

        if(product.getIsDeleted()){
            message = "Product with id "+id+ " not found";
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        ProductAdminViewDto productAdminViewDto = new ProductAdminViewDto();
        ProductSellerDto productDto = toProductSellerDto(product);
        productDto.setCategoryDto(categoryService.toCategoryDto(product.getCategory()));
        productAdminViewDto.setProductDto(productDto);
        // add primary images of all the variations in this dto.

        response = new ResponseVO<ProductAdminViewDto>(productAdminViewDto, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getAllProductsForAdmin(Long categoryId, String offset, String size, String sortByField, String order, String brand) {
        BaseVO response;

        Pageable pageable = pagingService.getPageableObject(offset, size, sortByField, order);

        List<Product> products;
        if(categoryId!=null && brand!=null){
            products = productRepository.findByBrandAndCategoryIdAndIsDeletedFalse(brand, categoryId, pageable);
        }
        else if(categoryId!=null){
            products = productRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable);
        }
        else if(brand != null){
            products = productRepository.findByBrandAndIsDeletedFalse(brand, pageable);
        }
        else{
            products = productRepository.findByIsDeletedFalse(pageable);
        }

        List<ProductAdminViewDto> productDtos = new ArrayList<>();
        products.forEach(product -> {
            ProductAdminViewDto viewDto = new ProductAdminViewDto();
            ProductSellerDto dto = toProductSellerDto(product);
            dto.setCategoryDto(categoryService.toCategoryDto(product.getCategory()));
            viewDto.setProductDto(dto);
            productDtos.add(viewDto);
            // u need to add the images of variations as well here.
        });

        response = new ResponseVO<List>(productDtos, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public void deleteProductById(Long p_id){
        // delete the product
        productRepository.deleteProductById(p_id);

        // delete all variations
        variationService.deleteVariationByProductId(p_id);

        // delete all reviews
        reviewRepository.deleteByProductId(p_id);
    }


    public void deleteAllProductsByCategoryId(Long c_id) {
       productRepository.deleteProductsByCategoryId(c_id);
    }

}

