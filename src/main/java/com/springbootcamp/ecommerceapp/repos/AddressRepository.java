package com.springbootcamp.ecommerceapp.repos;

import com.springbootcamp.ecommerceapp.entities.Address;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

    @Modifying
    @Transactional
    @Query(value = "update address set is_deleted=true, user_id=null where id= :Id", nativeQuery = true)
    void deleteAddressById(@Param("Id") Long Id);

    Optional<Address> findByIdAndIsDeletedFalse(Long id);
}
