package com.springbootcamp.ecommerceapp.entities;

import com.springbootcamp.ecommerceapp.utils.CategoryMetadataFieldValuesId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Where(clause = "is_deleted='false'")
public class CategoryMetadataFieldValues extends AuditInformation{

    @EmbeddedId
    private CategoryMetadataFieldValuesId id = new CategoryMetadataFieldValuesId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("categoryId")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("categoryMetadataFieldId")
    private CategoryMetadataField categoryMetadataField;

    private String value;
    private Boolean isDeleted = false;

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
