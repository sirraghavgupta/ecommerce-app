package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.dtos.*;
import com.springbootcamp.ecommerceapp.entities.*;
import com.springbootcamp.ecommerceapp.repos.CategoryMetadataFieldRepository;
import com.springbootcamp.ecommerceapp.repos.CategoryMetadataFieldValuesRepository;
import com.springbootcamp.ecommerceapp.repos.CategoryRepository;
import com.springbootcamp.ecommerceapp.repos.ProductRepository;
import com.springbootcamp.ecommerceapp.utils.*;
import org.aspectj.weaver.patterns.PerObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMetadataFieldService fieldService;

    @Autowired
    PagingService pagingService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryMetadataFieldRepository fieldRepository;

    @Autowired
    CategoryMetadataFieldValuesRepository valuesRepository;

    @Autowired
    ModelMapper modelMapper;

    CategoryDto toCategoryDto(Category category){
        if(category == null)
            return null;
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        CategoryDto parentDto = toCategoryDto(category.getParentCategory());
        categoryDto.setParent(parentDto);
        return categoryDto;
    }

    CategoryDto toCategoryDtoNonRecursive(Category category){
        if(category == null)
            return null;
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        categoryDto.setParent(null);
        return categoryDto;
    }

    private String validateNewCategory(String categoryName, Long parentId) {
        if(categoryName==null || categoryName.equalsIgnoreCase("")){
            return "category name not valid.";
        }

        if(parentId == null){
            Category preStored = categoryRepository.findByNameAndIsDeletedFalse(categoryName);
            if(preStored == null)
                return "valid";
            return "Category already exists. So, can not insert at the root level.";
        }

        // check uniqueness at the root level
        List<Category> preStored = categoryRepository.findByParentIdIsNullAndIsDeletedFalse();
        for(Category c : preStored){
            if(c.getName().equalsIgnoreCase(categoryName))
                return "Category already exists at the root level.";
        }

        Category parent = categoryRepository.findByIdAndIsDeletedFalse(parentId).get();

        // check immediate children
        Set<Category> children = parent.getSubCategories();
        for(Category c : children){
            if(c.getName().equalsIgnoreCase(categoryName))
                return "Sibling category exists with same name.";
        }

        // check product associations of parent
        Set<Product> products = parent.getProducts();
        if(!products.isEmpty())
            return "Parent category has product associations.";

        // check path from parent to root
        while(parent!=null){
            if(parent.getName().equalsIgnoreCase(categoryName))
                return "Category already exists as ancestor.";
            parent = parent.getParentCategory();
        }

        return "valid";
    }

    public ResponseEntity<BaseVO> createNewCategory(String categoryName, Long parentId) {
        BaseVO response;
        String message = validateNewCategory(categoryName, parentId);
        if(!message.equals("valid")){
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        Category category = new Category(categoryName);
        Category parent;
        if(parentId==null){
            category.setParentCategory(null);
            categoryRepository.save(category);
        }
        else{
            Optional<Category> parentCategory = categoryRepository.findByIdAndIsDeletedFalse(parentId);
            if(!parentCategory.isPresent()){
                response = new ErrorVO("Invalid input", "Parent category not found", new Date());
                return new ResponseEntity<BaseVO>(response, HttpStatus.CONFLICT);
            }
            else{
                parent = parentCategory.get();
                category.setParentCategory(parent);
                parent.addSubCategory(category);
                categoryRepository.save(parent);
            }
        }
        response = new ResponseVO<Category>(null, "Success", new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.CREATED);
    }

    public CategoryAdminResponseDto toCategoryAdminResponseDto(Category category){

        CategoryAdminResponseDto categoryAdminResponseDto = new CategoryAdminResponseDto();

        // get actual category with all parent tree
        CategoryDto categoryDto = toCategoryDto(category);
        categoryAdminResponseDto.setCategory(categoryDto);

        // get child categories
        Set<CategoryDto> subCategories;         // for response

        List<Category> childCategories =        // actual
                categoryRepository.findAllSubCategoriesByCategoryId(category.getId());
        if(childCategories != null) {
            subCategories = new HashSet<>();

            childCategories.forEach((e) -> {
                subCategories.add(toCategoryDto(e));
            });
            categoryAdminResponseDto.setSubCategories(subCategories);
        }

        // get the possible metadata fields and values
        Set<CategoryMetadataFieldDto> fieldValues;
        List<CategoryMetadataFieldValues> actualFieldValues =
                valuesRepository.findAllFieldsAndValuesForLeafCategory(category.getId());

        if(actualFieldValues != null) {
            fieldValues = new HashSet<>();

            actualFieldValues.forEach((e) -> {
                CategoryMetadataFieldDto dto = fieldService.toCategoryMetadataFieldDto(e.getCategoryMetadataField());
                dto.setValues(StringToSetParser.toSetOfValues(e.getValue()));
                fieldValues.add(dto);
            });
            categoryAdminResponseDto.setFieldValues(fieldValues);
        }
        return categoryAdminResponseDto;
    }

    public ResponseEntity<BaseVO> getCategoryAllDetails(Long categoryId) {
        BaseVO response;
        Optional<Category> preStored = categoryRepository.findByIdAndIsDeletedFalse(categoryId);
        if(!preStored.isPresent()){
            response = new ErrorVO("Not Found", "Category with given id does not exist.", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        CategoryAdminResponseDto categoryAdminResponseDto = toCategoryAdminResponseDto(preStored.get());

        response = new ResponseVO<CategoryAdminResponseDto>(categoryAdminResponseDto, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getAllCategories(String offset, String size, String sortByField, String order) {
        BaseVO response;

        Pageable pageable = pagingService.getPageableObject(offset, size, sortByField, order);

        List<Category> categories = categoryRepository.findByIsDeletedFalse(pageable);
        List<CategoryAdminResponseDto> categoryDtos = new ArrayList<>();

        categories.forEach((category)->{
            categoryDtos.add(toCategoryAdminResponseDto(category));
        });

        response = new ResponseVO<List>(categoryDtos, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getAllCategoriesForSeller() {
        BaseVO response;
        List<Category> parentCategories = categoryRepository.findAllParentCategories();
        List<Category> allCategories = categoryRepository.findByIsDeletedFalse();
        allCategories.removeAll(parentCategories);

        List<CategoryAdminResponseDto> categoryDtos = new ArrayList<>();

        allCategories.forEach((category)->{
            categoryDtos.add(toCategoryAdminResponseDto(category));
        });

        response = new ResponseVO<List>(categoryDtos, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getAllCategoriesForCustomer(Long id) {
        BaseVO response;
        if(id==null) {
            List<Category> rootCategories = categoryRepository.findByParentIdIsNullAndIsDeletedFalse();
            List<CategoryDto> categoryDtos = new ArrayList<>();
            rootCategories.forEach((e) -> {
                categoryDtos.add(toCategoryDto(e));
            });
            response = new ResponseVO<List>(categoryDtos, null, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
        }
        Optional<Category> savedCategory = categoryRepository.findByIdAndIsDeletedFalse(id);
        if(!savedCategory.isPresent()){
            response = new ErrorVO("Not Found", "Category does not exist.", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        Category category = savedCategory.get();
        Set<Category> subCategories = category.getSubCategories();
        List<CategoryDto> subCategoryDtos = new ArrayList<>();

        subCategories.forEach((e)->{
            subCategoryDtos.add(toCategoryDto(e));
        });
        response = new ResponseVO<List>(subCategoryDtos, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> deleteCategoryById(Long id) {
        BaseVO response;
        Optional<Category> savedCategory = categoryRepository.findByIdAndIsDeletedFalse(id);
        if(!savedCategory.isPresent()){
            response = new ErrorVO("Not Found", "Category does not exist.", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        List<Category> subCategories = categoryRepository.findAllSubCategoriesByCategoryId(id);
        if(subCategories!=null && !subCategories.isEmpty()){
            response = new ErrorVO("Validation failed", "Parent category can not be deleted.", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        List<Product> products = productRepository.findByCategoryIdAndIsDeletedFalse(id);
        if(products!=null && !products.isEmpty()){
            response = new ErrorVO("Validation failed", "Category with " + id +
                            " has some products associated with it.", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        deleteCategoryByCategoryId(id);

        response = new ResponseVO<String>(null, "Success", new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> updateCategory(Long id, String name) {
        BaseVO response;
        String message = validateCategoryUpdate(id, name);
        if(!message.equals("valid")){
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id).get();
        category.setName(name);
        categoryRepository.save(category);

        response = new ResponseVO<String>(null, "Success", new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> addMetadataFieldValuePair(MetadataFieldValueInsertDto fieldValueDtos) {
        BaseVO response;
        String message = validateCategoryMetadataFieldDto(fieldValueDtos, "creation");
        if(!message.equalsIgnoreCase("success")){
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        return createMetadataFieldValuePair(fieldValueDtos);
    }

    public String validateCategoryMetadataFieldDto(MetadataFieldValueInsertDto fieldValueDtos, String purpose) {
        BaseVO response;
        String message;

        Optional<Category> savedCategory = categoryRepository.findByIdAndIsDeletedFalse(fieldValueDtos.getCategoryId());
        if(!savedCategory.isPresent()){
            message = "Category does not exist.";
            return message;
        }

        Category category = savedCategory.get();
        for(CategoryMetadataFieldDto fieldValuePair : fieldValueDtos.getFieldValues()){
            Optional<CategoryMetadataField> field = fieldRepository.findByIdAndIsDeletedFalse(fieldValuePair.getId());
            if(!field.isPresent()){
                message = "Field id "+fieldValuePair.getId()+" not found";
                return message;
            }

            if(purpose.equalsIgnoreCase("creation")){
                List<CategoryMetadataFieldValues> fieldValues =
                        valuesRepository.findByCategoryMetadataFieldIdAndIsDeletedFalse(field.get().getId());
                if(fieldValues != null && !fieldValues.isEmpty()){
                    message = "Field values already exist for field "+fieldValuePair.getId();
                }
            }
            else{
                Optional<CategoryMetadataFieldValues> savedMetadataFieldValue;
                CategoryMetadataFieldValuesId fieldValuePairId =
                        new CategoryMetadataFieldValuesId(category.getId(), fieldValuePair.getId());

                savedMetadataFieldValue = valuesRepository.findByIdAndIsDeletedFalse(fieldValuePairId);
                if(!savedMetadataFieldValue.isPresent()){
                    message = "Field with id - " + fieldValuePair.getId() + " does not belong to this category.";
                    return message;
                }
            }

            // if field is found valid, then check for values
            if(fieldValuePair.getValues().isEmpty()){
                message = "No field values provided to insert for field id "+fieldValuePair.getId();
                return message;
            }
            //duplicate values error can not occur as I am storing values in a set.
        }

        message = "success";
        return message;
    }

    public ResponseEntity<BaseVO> createMetadataFieldValuePair(MetadataFieldValueInsertDto fieldValueDtos) {
        BaseVO response;
        Category category = categoryRepository.findByIdAndIsDeletedFalse(fieldValueDtos.getCategoryId()).get();
        CategoryMetadataFieldValues categoryFieldValues;
        CategoryMetadataField categoryField;

        for(CategoryMetadataFieldDto fieldValuePair : fieldValueDtos.getFieldValues()){
            categoryFieldValues = new CategoryMetadataFieldValues();
            categoryField = fieldRepository.findByIdAndIsDeletedFalse(fieldValuePair.getId()).get();
            String values = StringToSetParser.toCommaSeparatedString(fieldValuePair.getValues());

            categoryFieldValues.setValue(values);
            categoryFieldValues.setCategory(category);
            categoryFieldValues.setCategoryMetadataField(categoryField);

            valuesRepository.save(categoryFieldValues);
        }
        response = new ResponseVO<String>(null, "Success", new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<BaseVO> updateMetadataFieldValuePair(MetadataFieldValueInsertDto fieldValueDtos) {
        BaseVO response;
        String message = validateCategoryMetadataFieldDto(fieldValueDtos, "updation");
        if(!message.equalsIgnoreCase("success")){
            response = new ErrorVO("Validation failed", message, new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.BAD_REQUEST);
        }

        Category category = categoryRepository.findByIdAndIsDeletedFalse(fieldValueDtos.getCategoryId()).get();
        Optional<CategoryMetadataFieldValues> savedMetadataFieldValue;
        CategoryMetadataFieldValues metadataFieldValue;
        List<CategoryMetadataFieldDto> fieldValuePairs = fieldValueDtos.getFieldValues();

        for(CategoryMetadataFieldDto fieldValuePair : fieldValuePairs){
            CategoryMetadataField field = fieldRepository.findByIdAndIsDeletedFalse(fieldValuePair.getId()).get();

            CategoryMetadataFieldValuesId fieldValuePairId =
                    new CategoryMetadataFieldValuesId(category.getId(), field.getId());

            savedMetadataFieldValue = valuesRepository.findByIdAndIsDeletedFalse(fieldValuePairId);
            String values = null;
            Set<String> valueSet;

            metadataFieldValue = savedMetadataFieldValue.get();
            values = metadataFieldValue.getValue();
            valueSet = StringToSetParser.toSetOfValues(values);

            for(String value : fieldValuePair.getValues()){
                valueSet.add(value);
            }

            values = StringToSetParser.toCommaSeparatedString(valueSet);

            metadataFieldValue.setValue(values);
            metadataFieldValue.setCategoryMetadataField(field);
            metadataFieldValue.setCategory(category);

            valuesRepository.save(metadataFieldValue);
        }
        response = new ResponseVO<String>(null, "Success", new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getFilteringDetailsForCategory(Long categoryId){
        BaseVO response;

        Optional<Category> savedCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId);
        if(!savedCategory.isPresent()){
            response = new ErrorVO("Not Found", "Category with id " + categoryId +" does not exist.", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        Category category = savedCategory.get();
        CategoryFilteringDetailsDto filterDto = new CategoryFilteringDetailsDto();

        PricePair pair = new PricePair();
        pair.minPrice = Double.POSITIVE_INFINITY;
        pair.maxPrice = 0d;
        getMinMaxPriceForCategory(category, pair);

        Map<String, String> map = new HashMap<>();
        getAllFieldValuePairsForCategory(category, map);

        Map<String, Set<String>> fieldValueMap = new HashMap<>();
        map.forEach((field, value)->{
            fieldValueMap.put(field, StringToSetParser.toSetOfValues(value));
        });

        filterDto.setBrands(getAllBrandsForCategory(categoryId));
        filterDto.setMaxPrice(pair.maxPrice);
        filterDto.setMinPrice(pair.minPrice);
        filterDto.setFieldValues(fieldValueMap);

        response = new ResponseVO<CategoryFilteringDetailsDto>(filterDto, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public Set<String> getAllBrandsForCategory(Long categoryId) {
        Optional<Category> savedCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId);
        if(!savedCategory.isPresent())
            return null;

        Set<String> brands = new HashSet<>();
        Category category = savedCategory.get();

        if(category.getSubCategories()==null || category.getSubCategories().isEmpty()){
            brands.addAll(productRepository.findAllBrandsByCategoryId(categoryId));
        }
        else{
            category.getSubCategories().forEach(child->{
                brands.addAll(getAllBrandsForCategory(child.getId()));
            });
        }
        return brands;
    }

    public void getMinMaxPriceForCategory(Category category, PricePair pair){
        // assuming that category already exists

        // if its a leaf category - iterate over the products
        if(category.getSubCategories() == null || category.getSubCategories().isEmpty()){

            // if category has some products
            if(category.getProducts()!=null && category.getProducts().isEmpty()==false){
                Set<Product> products = category.getProducts();
                for (Product product : products) {

                    // if product has some variations
                    if(product.getVariations()!=null && product.getVariations().isEmpty()==false){
                        Set<ProductVariation> variations = product.getVariations();

                        for (ProductVariation variation : variations) {
                            if(variation.getPrice() < pair.minPrice) {
                                pair.minPrice = variation.getPrice();
                                System.out.println("minPrice found"  + pair.minPrice);
                            }

                            if(variation.getPrice() > pair.maxPrice){
                                pair.maxPrice = variation.getPrice();
                                System.out.println("maxPrice found" + pair.maxPrice);
                            }
                        }
                    }
                }
            }
        }

        else {
            // now if category is a parent category
            Set<Category> subCategories = category.getSubCategories();
            for (Category subCategory : subCategories) {
                getMinMaxPriceForCategory(subCategory, pair);
            }
        }

    }

    public void getAllFieldValuePairsForCategory(Category category, Map<String, String> fieldValueMap){

        // assume that the category exists. check before calling the method.

        if(category.getSubCategories() != null && !category.getSubCategories().isEmpty()){

            Set<Category> subCategories = category.getSubCategories();
            for(Category child : subCategories){
                getAllFieldValuePairsForCategory(child, fieldValueMap);
            }
        }

        else{
            List<CategoryMetadataFieldValues> fieldValuePairs =
                    valuesRepository.findAllFieldsAndValuesForLeafCategory(category.getId());

            Set<String> fields = fieldValueMap.keySet();

            for(CategoryMetadataFieldValues pair : fieldValuePairs){
                String fieldName = pair.getCategoryMetadataField().getName();
                if(fields.contains(fieldName)){
                    String values = fieldValueMap.get(fieldName);
                    values = values + "," +pair.getValue();
                    fieldValueMap.put(fieldName, values);
                }
                else{
                    fieldValueMap.put(fieldName, pair.getValue());
                }
            }
        }
    }

    public void deleteCategoryByCategoryId(Long c_id){
        // it should be a leaf category only with no associated products.

        // delete the category itself
        categoryRepository.deleteCategoryById(c_id);

        // delete all field values
        valuesRepository.deleteValuesByCategoryId(c_id);
    }

    public String validateCategoryUpdate(Long id, String categoryName){
        Optional<Category> savedCategory = categoryRepository.findByIdAndIsDeletedFalse(id);
        String message;

        if(!savedCategory.isPresent()){
            message = "Category does not exist.";
            return message;
        }
        Category category = savedCategory.get();
        Category parent = category.getParentCategory();
        Long parentId = null;
        if(parent != null){
            parentId = parent.getId();
        }
        message =  validateNewCategory(categoryName, parentId);
        if(!message.equalsIgnoreCase("valid")){
            return message;
        }

        List<Category> subCategories = categoryRepository.findAllSubCategoriesByCategoryId(id);
        if(subCategories!=null && !subCategories.isEmpty()){
            for (Category subCategory : subCategories) {
                if(subCategory.getName().equalsIgnoreCase(categoryName)){
                    message = "Child category with this name already exists.";
                    return message;
                }
            }

        }
        return "valid";
    }
}
