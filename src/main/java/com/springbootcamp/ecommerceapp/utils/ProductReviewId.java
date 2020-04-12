package com.springbootcamp.ecommerceapp.utils;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProductReviewId implements Serializable {

    private Long customerUserId;
    private Long productId;

}

