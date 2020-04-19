package com.springbootcamp.ecommerceapp.services;

import com.springbootcamp.ecommerceapp.dtos.CategoryMetadataFieldDto;
import com.springbootcamp.ecommerceapp.entities.Category;
import com.springbootcamp.ecommerceapp.entities.CategoryMetadataField;
import com.springbootcamp.ecommerceapp.entities.CategoryMetadataFieldValues;
import com.springbootcamp.ecommerceapp.repos.CategoryMetadataFieldRepository;
import com.springbootcamp.ecommerceapp.repos.CategoryMetadataFieldValuesRepository;
import com.springbootcamp.ecommerceapp.repos.CategoryRepository;
import com.springbootcamp.ecommerceapp.utils.ErrorVO;
import com.springbootcamp.ecommerceapp.utils.ResponseVO;
import com.springbootcamp.ecommerceapp.utils.BaseVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CategoryMetadataFieldService {
    
    @Autowired
    CategoryMetadataFieldRepository fieldRepository;

    @Autowired
    CategoryMetadataFieldValuesRepository valuesRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PagingService pagingService;

    @Autowired
    ModelMapper modelMapper;

    public CategoryMetadataField toCategoryMetadataField(CategoryMetadataFieldDto fieldDto){
        if(fieldDto == null)
            return null;
        return modelMapper.map(fieldDto, CategoryMetadataField.class);
    }

    public CategoryMetadataFieldDto toCategoryMetadataFieldDto(CategoryMetadataField field){
        if(field == null)
            return null;
        return modelMapper.map(field, CategoryMetadataFieldDto.class);
    }

    public ResponseEntity<BaseVO> addNewMetadataField(String fieldName) {
        CategoryMetadataField savedField = fieldRepository.findByNameAndIsDeletedFalse(fieldName);
        BaseVO response;
        if(savedField!=null){
            response = new ErrorVO("Invalid operation", "Field Name already exists", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.CONFLICT);
        }

        savedField = new CategoryMetadataField();
        savedField.setName(fieldName);
        fieldRepository.save(savedField);
        response = new ResponseVO<CategoryMetadataField>(null, "Category metadata field created", new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<BaseVO> getAllMetadataFields(String offset, String size, String sortByField, String order) {
        BaseVO response;

        Pageable pageable = pagingService.getPageableObject(offset, size, sortByField, order);

        List<CategoryMetadataField> fields = fieldRepository.findAll(pageable);
        List<CategoryMetadataFieldDto> responseData = new ArrayList<>();

        fields.forEach((field)->{
            CategoryMetadataFieldDto dto = toCategoryMetadataFieldDto(field);
            dto.setValues(null);
            responseData.add(dto);
        });

        response = new ResponseVO<List>(responseData, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseVO> getAllMetadataFieldsByCategoryId(String offset, String size, String sortByField, String order, Long categoryId) {
        Optional<Category> savedCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId);
        BaseVO response;

        Pageable pageable = pagingService.getPageableObject(offset, size, sortByField, order);

        if(!savedCategory.isPresent()){
            response = new ErrorVO("Not found", "No category exists with this id.", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }


        Category category = savedCategory.get();
        Set<CategoryMetadataFieldValues> fieldValues = category.getFieldValues();

        if(fieldValues == null || fieldValues.isEmpty()){
            response = new ResponseVO<Set>(null, "No metadata fields are associated with this category.", new Date());
            return new ResponseEntity<BaseVO>(response, HttpStatus.NOT_FOUND);
        }

        List<CategoryMetadataFieldDto> fields = new ArrayList<>();

        fieldValues.forEach((e)->{
            CategoryMetadataFieldDto dto = toCategoryMetadataFieldDto(e.getCategoryMetadataField());
            dto.setValues(null);
            fields.add(dto);
        });
        response = new ResponseVO<List>(fields, null, new Date());
        return new ResponseEntity<BaseVO>(response, HttpStatus.OK);
    }

    public void deleteFieldById(Long id){
        // delete the field itself first
        fieldRepository.deleteFieldById(id);

        // delete all the field-values first
        valuesRepository.deleteValuesByFieldId(id);
    }

}
