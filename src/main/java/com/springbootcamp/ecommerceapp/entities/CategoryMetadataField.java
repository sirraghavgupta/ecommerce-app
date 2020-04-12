package com.springbootcamp.ecommerceapp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.valueextraction.UnwrapByDefault;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CategoryMetadataField {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

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
