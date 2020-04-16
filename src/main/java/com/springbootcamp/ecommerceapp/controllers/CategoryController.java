package com.springbootcamp.ecommerceapp.controllers;


import com.springbootcamp.ecommerceapp.dtos.MetadataFieldValueInsertDto;
import com.springbootcamp.ecommerceapp.services.CategoryMetadataFieldService;
import com.springbootcamp.ecommerceapp.services.CategoryService;
import com.springbootcamp.ecommerceapp.utils.BaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryMetadataFieldService fieldService;

    @PostMapping("/metadata-fields")
    public ResponseEntity<BaseVO> addMetaDataField(@RequestParam String fieldName) {
        return fieldService.addNewMetadataField(fieldName);
    }

    @GetMapping("/metadata-fields")
    public ResponseEntity<BaseVO> getAllMetadataFields(@RequestParam(defaultValue = "0") String offset,
                                                       @RequestParam(defaultValue = "10") String size,
                                                       @RequestParam(defaultValue = "id") String sortByField,
                                                       @RequestParam(defaultValue = "ascending") String order,
                                                       @RequestParam(required = false) Long categoryId) {
        if (categoryId != null)
            return fieldService.getAllMetadataFieldsByCategoryId(offset, size, sortByField, order, categoryId);

        return fieldService.getAllMetadataFields(offset, size, sortByField, order);
    }


    @PostMapping("/categories")
    public ResponseEntity<BaseVO> addCategory(@RequestParam String categoryName,
                                              @RequestParam(required = false) Long parentId) {
        return categoryService.createNewCategory(categoryName, parentId);
    }


    @GetMapping("/category/{id}")
    public ResponseEntity<BaseVO> getCategoryDetails(@PathVariable(name = "id") Long categoryId) {
        return categoryService.getCategoryAllDetails(categoryId);
    }

    @GetMapping("/categories")
    public ResponseEntity<BaseVO> getAllCategories(@RequestParam(defaultValue = "0") String offset,
                                                   @RequestParam(defaultValue = "10") String size,
                                                   @RequestParam(defaultValue = "id") String sortByField,
                                                   @RequestParam(defaultValue = "ascending") String order) {

        return categoryService.getAllCategories(offset, size, sortByField, order);
    }


    @DeleteMapping("/category/{id}")
    public ResponseEntity<BaseVO> deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategoryById(id);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<BaseVO> updateCategory(@PathVariable Long id, @RequestParam String name){
        return categoryService.updateCategory(id, name);
    }

    @PostMapping("/metadata-field-values")
    public ResponseEntity<BaseVO> addMetadataFieldValues(@RequestBody MetadataFieldValueInsertDto fieldValueDtos){
        return categoryService.addMetadataFieldValuePair(fieldValueDtos);
    }

    @PutMapping("/metadata-field-values")
    public ResponseEntity<BaseVO> updateMetadataFieldValues(@RequestBody MetadataFieldValueInsertDto fieldValueDtos){
        return categoryService.updateMetadataFieldValuePair(fieldValueDtos);
    }

    @GetMapping("/categories/seller")
    public ResponseEntity<BaseVO> getAllCategories(){
        return categoryService.getAllCategoriesForSeller();
    }

    @GetMapping("/categories/customer")
    public ResponseEntity<BaseVO> getAllCategories(@RequestParam(required = false) Long id){
        return categoryService.getAllCategoriesForCustomer(id);
    }


}
