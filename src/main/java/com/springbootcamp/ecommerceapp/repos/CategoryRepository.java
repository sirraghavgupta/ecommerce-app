package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByIdAndIsDeletedFalse(Long id);

    Category findByName(String name);
    Category findByNameAndIsDeletedFalse(String name);

    @Query(value = "select * from category where parent_id is null", nativeQuery = true)
    List<Category> findByParentIdIsNull();

    @Query(value = "select * from category where parent_id is null and is_deleted=false", nativeQuery = true)
    List<Category> findByParentIdIsNullAndIsDeletedFalse();

    List<Category> findAll();
    List<Category> findByIsDeletedFalse();

    List<Category> findAll(Pageable pageable);
    List<Category> findByIsDeletedFalse(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update category set is_deleted=true where id = :c_id", nativeQuery = true)
    void deleteCategoryById(@Param("c_id") Long c_id);

    @Modifying
    @Transactional
    @Query(value = "update category set is_deleted=true where parent_id = :c_id", nativeQuery = true)
    void deleteSubCategoriesByParentCategoryId(@Param("c_id") Long c_id);

    @Query(value = "select * from category where parent_id = :c_id and is_deleted=false", nativeQuery = true)
    List<Category> findAllSubCategoriesByCategoryId(@Param("c_id") Long c_id);


    @Query(value = "select * from category where parent_id = :c_id and is_deleted=false", nativeQuery = true)
    List<Category> findAllSubCategoriesOfCategory(@Param("c_id") Long c_id);


//    @Query(value = "select * from category where id not in(select distinct c.parent_id from category c where c.is_deleted=false))", nativeQuery = true)
//    List<Category> findAllLeafCategories();
}



