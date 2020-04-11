package com.springbootcamp.ecommerceapp.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class VO{
    private String message;
    private Date timestamp;

    public VO(String message, Date timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "VO{" +
                "message='" + message + '\'' +
                ", date=" + timestamp +
                '}';
    }
}
