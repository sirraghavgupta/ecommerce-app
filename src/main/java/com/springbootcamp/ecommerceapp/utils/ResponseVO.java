package com.springbootcamp.ecommerceapp.utils;


import java.util.Date;

public class ResponseVO<T> extends VO{

    private T data;

    public ResponseVO() {
    }

    public ResponseVO(T data, String message, Date timestamp) {
        super(message, timestamp);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
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
