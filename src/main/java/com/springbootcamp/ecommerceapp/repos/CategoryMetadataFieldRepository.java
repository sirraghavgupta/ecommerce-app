package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.CategoryMetadataField;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetadataFieldRepository extends CrudRepository<CategoryMetadataField, Long> {

}
