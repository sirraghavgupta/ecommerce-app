package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.CategoryMetadataField;
import com.springbootcamp.ecommerceapp.entities.CategoryMetadataFieldValues;
import com.springbootcamp.ecommerceapp.utils.CategoryMetadataFieldValuesId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMetadataFieldValuesRepository extends CrudRepository<CategoryMetadataFieldValues, CategoryMetadataFieldValuesId> {

    @Query(value = "select f.id, f.name from category_metadata_field_values v " +
            "inner join " +
            "category_metadata_field f where v.category_metadata_field_id = f.id " +
            "and v.category_id= :categoryId", nativeQuery = true)
    List<Object[]> findMetadataFieldsByCategoryId(Long categoryId);

}
