package com.springbootcamp.ecommerceapp.utils;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CartId implements Serializable {

    private Long customerUserId;
    private Long productVariationId;

}
