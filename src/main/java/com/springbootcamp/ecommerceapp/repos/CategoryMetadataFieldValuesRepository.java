package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.CategoryMetadataFieldValues;
import com.springbootcamp.ecommerceapp.utils.CategoryMetadataFieldValuesId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetadataFieldValuesRepository extends CrudRepository<CategoryMetadataFieldValues, CategoryMetadataFieldValuesId> {

}
