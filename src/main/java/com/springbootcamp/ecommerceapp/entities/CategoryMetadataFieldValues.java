package com.springbootcamp.ecommerceapp.entities;

import com.springbootcamp.ecommerceapp.utils.CategoryMetadataFieldValuesId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CategoryMetadataFieldValues {

    @EmbeddedId
    private CategoryMetadataFieldValuesId id = new CategoryMetadataFieldValuesId();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapsId("categoryId")
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapsId("categoryMetadataFieldId")
    private CategoryMetadataField categoryMetadataField;

    private String value;

    public CategoryMetadataFieldValues(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CategoryMetadataFieldValues{" +
                "id=" + id +
                ", category=" + category.getName() +
                ", categoryMetadataField=" + categoryMetadataField.getName() +
                ", values='" + value + '\'' +
                '}';
    }
}
