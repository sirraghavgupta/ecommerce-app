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
import com.springbootcamp.ecommerceapp.utils.VO;
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

    public ResponseEntity<VO> addNewMetadataField(String fieldName) {
        CategoryMetadataField savedField = fieldRepository.findByName(fieldName);
        VO response;
        if(savedField!=null){
            response = new ErrorVO("Invalid operation", "Field Name already exists", new Date());
            return new ResponseEntity<VO>(response, HttpStatus.CONFLICT);
        }

        savedField = new CategoryMetadataField();
        savedField.setName(fieldName);
        fieldRepository.save(savedField);
        response = new ResponseVO<CategoryMetadataField>(null, "Category metadata field created", new Date());
        return new ResponseEntity<VO>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<VO> getAllMetadataFields(String offset, String size, String sortByField, String order) {
        VO response;
        Integer pageNo = Integer.parseInt(offset);
        Integer pageSize = Integer.parseInt(size);

        Pageable pageable;
        if(order.equals("ascending"))
             pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortByField).ascending());
        else
             pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortByField).descending());

        List<CategoryMetadataField> fields = fieldRepository.findAll(pageable);
        List<CategoryMetadataFieldDto> responseData = new ArrayList<>();

        fields.forEach((field)->{
            CategoryMetadataFieldDto dto = toCategoryMetadataFieldDto(field);
            dto.setValues(null);
            responseData.add(dto);
        });

        response = new ResponseVO<List>(responseData, null, new Date());
        return new ResponseEntity<VO>(response, HttpStatus.OK);
    }

    public ResponseEntity<VO> getAllMetadataFieldsByCategoryId(String offset, String size, String sortByField, String order, Long categoryId) {
        Optional<Category> savedCategory = categoryRepository.findById(categoryId);
        VO response;
        Integer pageNo = Integer.parseInt(offset);
        Integer pageSize = Integer.parseInt(size);

        Pageable pageable;
        if(order.equals("ascending"))
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortByField).ascending());
        else
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortByField).descending());


        if(!savedCategory.isPresent()){
            response = new ErrorVO("Not found", "No category exists with this id.", new Date());
            return new ResponseEntity<VO>(response, HttpStatus.NOT_FOUND);
        }


        Category category = savedCategory.get();
        Set<CategoryMetadataFieldValues> fieldValues = category.getFieldValues();

        if(fieldValues == null || fieldValues.isEmpty()){
            response = new ResponseVO<Set>(null, "No metadata fields are associated with this category.", new Date());
            return new ResponseEntity<VO>(response, HttpStatus.NOT_FOUND);
        }

        List<CategoryMetadataFieldDto> fields = new ArrayList<>();

        fieldValues.forEach((e)->{
            CategoryMetadataFieldDto dto = toCategoryMetadataFieldDto(e.getCategoryMetadataField());
            dto.setValues(null);
            fields.add(dto);
        });
        response = new ResponseVO<List>(fields, null, new Date());
        return new ResponseEntity<VO>(response, HttpStatus.OK);
    }
}
