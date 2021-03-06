package com.springbootcamp.ecommerceapp.utils;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ErrorVO extends BaseVO {

    private String error;

    public ErrorVO(String error, String message, Date timestamp) {
        super(message, timestamp);
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorVO{" +
                super.toString() +
                "error='" + error + '\'' +
                '}';
    }
}
