package com.springbootcamp.ecommerceapp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.valueextraction.UnwrapByDefault;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Where(clause = "is_deleted=false")
public class CategoryMetadataField extends AuditInformation{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Boolean isDeleted=false;

    @OneToMany(mappedBy = "categoryMetadataField", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CategoryMetadataFieldValues> fieldValues;

    public CategoryMetadataField(String name) {
        this.name = name;
    }

    public void addFieldValues(CategoryMetadataFieldValues fieldValue){
        if(fieldValue != null){
            if(fieldValues==null)
                fieldValues = new HashSet<>();

            fieldValues.add(fieldValue);
            fieldValue.setCategoryMetadataField(this);
        }
    }

    @Override
    public String toString() {
        return "CategoryMetadataField{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
