package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.CategoryMetadataField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryMetadataFieldRepository extends CrudRepository<CategoryMetadataField, Long> {

    Optional<CategoryMetadataField> findById(Long id);
    Optional<CategoryMetadataField> findByIdAndIsDeletedFalse(Long id);

    CategoryMetadataField findByName(String fieldName);
    CategoryMetadataField findByNameAndIsDeletedFalse(String fieldName);

    List<CategoryMetadataField> findAll();
    List<CategoryMetadataField> findByIsDeletedFalse();

    List<CategoryMetadataField> findAll(Pageable pageable);
    List<CategoryMetadataField> findByIsDeletedFalse(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update category_metadata_field set is_deleted=true where id = :f_id", nativeQuery = true)
    void deleteFieldById(@Param("f_id") Long f_id);

}
