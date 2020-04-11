package com.springbootcamp.ecommerceapp.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ResponseVO<T> extends VO{

    private T data;

    public ResponseVO(T data, String message, Date timestamp) {
        super(message, timestamp);
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseVO{" +
                super.toString() +
                "data=" + data +
                '}';
    }
}
