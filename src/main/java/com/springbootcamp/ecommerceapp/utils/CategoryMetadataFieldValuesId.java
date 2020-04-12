package com.springbootcamp.ecommerceapp.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CategoryMetadataFieldValuesId implements Serializable{

    private Long categoryId;
    private Long categoryMetadataFieldId;

}
